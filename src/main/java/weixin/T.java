package weixin;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
/**
 * 对资源文件的修改，添加
 * @author zhao
 *
 */
public class T {


/**
* @param args
*/
public static void main(String[] args) {
	String filePath = "C:\\aa.properties";  
    Properties prop = new Properties();  
    try {  
        InputStream fis = new FileInputStream(filePath);  
        // 从输入流中读取属性列表（键和元素对）  
        prop.load(fis);  
        // 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。  
        // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。  
        OutputStream fos = new FileOutputStream(filePath);  
        prop.setProperty("name", "testssdd");  
        // 以适合使用 load 方法加载到 Properties 表中的格式，  
        // 将此 Properties 表中的属性列表（键和元素对）写入输出流  
        prop.store(fos, "Update '" + "ddd" + "' value");  
    } catch (IOException e) {  
        System.err.println("Visit " + filePath + " for updating " + "ddd"  
                + " value error");  
    }
}
/**
* 修改/添加AutoAnalysisTime.properties资源文件中键值对;
* 如果K值原先存在则，修改该K值对应的value值;
* 如果K值原先不存在则，添加该键值对到资源中.
* @param key
* @param value
* @author zzb
*/
public static void update_properies(String key,String value){
   String path ="../../adderss.properties";
   File file = new File(path); 
   Properties prop = new Properties(); 
   InputStream inputFile = null;  
        OutputStream outputFile = null;  
        try {  
            inputFile = new FileInputStream(file);  
            prop.load(inputFile);  
           // 
            inputFile.close();//一定要在修改值之前关闭inputFile  
            outputFile = new FileOutputStream(file); 
          //设值-保存
            prop.setProperty(key, value); 
            //添加注释
            prop.store(outputFile, "Update '" + key + "'+ '"+value);  
        } catch (IOException e) {
       e.printStackTrace();  
        }  
        finally{  
            try {  
           if(null!=outputFile){
           outputFile.close();  
           }
            } catch (IOException e) {  
                e.printStackTrace();  
            } 
            try {  
           if(null!=inputFile){
           inputFile.close(); 
           } 
            } catch (IOException e) {  
                e.printStackTrace();  
            } 
        }
           
 }


} 