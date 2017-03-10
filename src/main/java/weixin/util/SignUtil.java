package weixin.util;

import java.util.Arrays;

public class SignUtil {
	  
    /** 
     * @param signature 
     * @param timestamp 
     * @param nonce 
     * @return 
     * @Author:lulei   
     * @Description: 微信权限验证 
     */  
    public static boolean checkSignature(String signature, String timestamp, String nonce) {  
        String[] arr = new String[] { "weixinstd", timestamp, nonce };  
        //按字典排序  
        Arrays.sort(arr);  
        StringBuilder content = new StringBuilder();    
        for (int i = 0; i < arr.length; i++) {    
            content.append(arr[i]);    
        }  
        //加密并返回验证结果  
        return signature == null ? false : signature.equals(Encrypt.encrypt(content.toString(), "SHA-1"));  
    }
    
}
