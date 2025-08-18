package com.nicepay.starter.common;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

/*
Copyright tPay
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

//http://aramk.tistory.com/32h 참조
public class AES256 {
    private String iv;
    private Key keySpec;
    
    public AES256(){}
    public AES256(String key) {
		try {
	        this.iv = key.substring(0, 16);
	        
	        byte[] keyBytes = new byte[16];
	        byte[] b;
			b = key.getBytes("UTF-8");
	        int len = b.length;
	        if (len > keyBytes.length) {
	            len = keyBytes.length;
	        }
	        System.arraycopy(b, 0, keyBytes, 0, len);
	        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
	        
	        this.keySpec = keySpec;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
 
    //AES 암호화
    public String encode(String str){
    	String enStr = null;
		try {
			Cipher c = null;
			c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			 byte[] encrypted = null;
			encrypted = c.doFinal(str.getBytes("UTF-8"));
	        enStr = new String(Base64.encodeBase64String(encrypted));
		} catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | 
				InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | 
				NoSuchPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return enStr;
    }
 
    //AES 복호화
    public String decode(String str) throws java.io.UnsupportedEncodingException,
                                                        NoSuchAlgorithmException,
                                                        NoSuchPaddingException, 
                                                        InvalidKeyException, 
                                                        InvalidAlgorithmParameterException,
                                                        IllegalBlockSizeException, 
                                                        BadPaddingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
 
        byte[] byteStr = Base64.decodeBase64(str.getBytes(), 0, str.length());
 
        return new String(c.doFinal(byteStr),"UTF-8");
    }
}
