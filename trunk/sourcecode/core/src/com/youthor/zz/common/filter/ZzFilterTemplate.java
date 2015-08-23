package com.youthor.zz.common.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.youthor.zz.common.util.ObjectUtil;
import com.youthor.zz.common.util.StringUtil;

public class ZzFilterTemplate implements ZzFilter{

    private final static String CONSTRUCTION_PATTERN = "\\{\\{[a-z]*(( )*([a-z]|[0-9])*=\"([^\"])*\"( )*)*\\}\\}";
    
    @Override
    public String filter(String text) {
        Pattern p = Pattern.compile(ZzFilterTemplate.CONSTRUCTION_PATTERN);
        Matcher m1 = p.matcher(text);
        Map<String, String> needReplace = new HashMap<String, String>();
        while(m1.find()) {
            MatchResult r1 = m1.toMatchResult();
            int start = r1.start()+2;
            int end = r1.end()-2;
            String regStr = text.substring(start, end);
            if (needReplace.containsKey("{{" + regStr + "}}")) continue;
            String [] regs = StringUtil.split(regStr, " ");
            String methodName = StringUtil.doWithNull(regs[0]) + "Directive";
            Map<String, String> paras = new HashMap<String, String>();
            for(int i = 1; i < regs.length; i ++) {
                String para = regs[i];
                if (StringUtil.isNotBlank(para)) {
                    String [] paraArr = StringUtil.split(para, "=");
                    if (paraArr.length == 2) {
                        paras.put(StringUtil.doWithNull(paraArr[0]), StringUtil.replaceAll(StringUtil.doWithNull(paraArr[1]), "\"", ""));
                    }
                }
            }
            Class<?>[] parameterTypes = new Class[1];
            parameterTypes[0] = Map.class;
            boolean b = ObjectUtil.isMethodExist(this, methodName, parameterTypes);
            String replaceStr = "";
            if (b) {
                Object [] data = new Object[1];
                data[0] = paras;
                replaceStr = (String)ObjectUtil.invokeMethod(this, methodName, parameterTypes, data);
            }
            needReplace.put("{{" + regStr + "}}", replaceStr);
        }
        if (needReplace.isEmpty()) return text;
        Set<String> keySet = needReplace.keySet();
        for(String key : keySet) {
           text = StringUtil.replaceAll(text, key, needReplace.get(key));
        }
        return text;
    }
}
