package com.changgou.filter;

public class URLFilter {

    private static String uri = "/api/user/add,/api/user/login";   // 无需拦截的url

    /**
     * @author 栗子 
     * @Description 判断是否是受保护的资源
     * @Date 19:41 2019/10/26
     * @param url
     * @return boolean
     **/
    public static boolean hasAuthorize(String url){
        String[] uris = uri.split(",");
        for (String uri : uris) {
            if (url.startsWith(uri)){
                return true;    // 无需登录
            }
        }
        return false;
    }
}
