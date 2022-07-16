import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.List;

public class V8_PR {
    static WebDriver driver=null;
    static File csvFile = new File("C:\\Users\\wnn\\Desktop\\GoogleDo wnLoad\\v8_Pr.csv");
    public static void main(String args[]) throws InterruptedException  {
        driver=new ChromeDriver();
        try {read(driver);}
        catch(Exception e) {e.printStackTrace();}
        finally {driver.quit();}
    }

    public static void openurl(String url) throws FileNotFoundException, UnsupportedEncodingException {

        OutputStreamWriter csvout;
        BufferedWriter csvbw;

        csvout = new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8");
        csvbw = new BufferedWriter(csvout);


        try {
            driver.get("url");
            Thread.sleep(3000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            driver.manage().window().maximize();
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            //查找review on的位置,这是评论的位置
            WebElement root6 = driver.findElement(By.cssSelector("#issue > div > div.container-issue-content > mr-issue-details > mr-comment-list"));
            SearchContext shadow_root6 = root6.getShadowRoot();

            List<WebElement> comments = shadow_root6.findElements(By.cssSelector("mr-comment"));
            String url1 = null;
            String result="";


            for(int i=0;i<comments.size();i++){
                String parent=comments.get(i).getText();
                String sub00 = "Reviewed-on:"; //以这个分界线进行分解
                String sub01 = "Reviewed-by:";

                boolean flag = false;
                int ind00 = 0;
                while (parent.indexOf(sub00)!= -1) {
                    ind00 = parent.lastIndexOf(sub00);

                    url1 = parent.substring(ind00+sub00.length()+1,ind00+sub00.length()+59);
                    flag = true;
                    break;
                }
                if(flag)
                    break;
            }

            String value="";
            value = "";
            System.out.println(url1);
            csvbw.write(value + "\n");

        } catch (Exception e) {

        }
    }


    public static void read(WebDriver driver) throws Exception {

        try {
            BufferedReader reade = new BufferedReader(new FileReader("D:\\PyCharm\\PythonWorkSpace\\v8_fixed_issue_url.csv"));//换成你的文件名
            String url = null;
            int index=0;
            reade.readLine();
            while((url=reade.readLine())!=null){
                openurl(url);
                }
            } catch (Exception ex) {
            throw new Exception(ex);
        }
    }
}

