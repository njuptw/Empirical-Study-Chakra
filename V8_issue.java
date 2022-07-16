import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
//import jxl.write.Label;
//import jxl.write.WritableSheet;
//import jxl.write.WritableWorkbook;
//import jxl.write.WriteException;
//import jxl.write.biff.RowsExceededException;



public class V8_issue {
    static WebDriver driver=null;
    public static void main(String args[]) throws InterruptedException  {
        driver=new ChromeDriver();
        try {test(driver);}
        catch(Exception e) {e.printStackTrace();}
        finally {driver.quit();}
    }


    public static void test(WebDriver driver) throws InterruptedException, IOException{

        File csvFile = new File("C:\\Users\\wnn\\Desktop\\GoogleDownLoad\\v8_Bug_Report.csv");


        String[] t = {"编号","title","reporter","start_time","Owner","Status","Modified","Components","OS","Priority","Type","Comment数量","第一个非报告者comment时间","Status更改时间","[modify][add]数量和","[modify][add]路径","test","patch" };
        String titles = t[0];
        for (int i = 1; i < t.length; i++) {
            titles = titles + "," + t[i];
        }
        OutputStreamWriter csvout;
        BufferedWriter csvbw;

        csvout = new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8");
        csvbw = new BufferedWriter(csvout);
        csvbw.write(titles + "\n");
        //需要修改的参数k每年
        // TODO: 2022/5/24  在这里修改需要的issue号
        for(int k=11276;k<=12886;k++){
            System.out.println(k);


            try {
                driver.get("https://bugs.chromium.org/p/v8/issues/detail?id="+k);
                Thread.sleep(3000);
            } catch (Exception e) {

                csvbw.write("网络异常"+ "\n");
                continue;
            }

            try {
                driver.manage().window().maximize();
                Thread.sleep(1000);
            } catch (Exception e) {

                csvbw.write("窗口最大化异常"+ "\n");
                continue;
            }

            try {
                WebElement root2 = driver.findElement(By.cssSelector("#issue > aside > mr-issue-metadata"));
                SearchContext shadow_root2 = root2.getShadowRoot();

                WebElement root3 = shadow_root2.findElement(By.cssSelector("mr-metadata"));
                SearchContext shadow_root3 = root3.getShadowRoot();

                WebElement status = shadow_root3.findElement(By.cssSelector("tr.row-status > td"));

                WebElement owner = shadow_root3.findElement(By.cssSelector("tr.row-owner > td"));

                WebElement modified = shadow_root3.findElement(By.cssSelector("tr.row-modified > td"));

                SimpleDateFormat sdf1 = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
                String t2="";
                try {
                    Date date2 = sdf1.parse(modified.getText());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    t2=simpleDateFormat.format(date2);
                    sdf1.format(new Date());
                } catch (ParseException e) {
                }

                WebElement Components = shadow_root3.findElement(By.cssSelector("tr.row-components > td"));


                int tag=0;
                try {
                    WebElement MergedInto = shadow_root3.findElement(By.cssSelector("tr.row-mergedinto"));
                    tag=1;
                } catch (Exception e) {
                    // TODO: handle exception
                }
                WebElement OS;WebElement Priority;WebElement Type;
                if(tag==1){
                    OS = shadow_root3.findElement(By.cssSelector("tr:nth-child(11) > td"));
                    Priority = shadow_root3.findElement(By.cssSelector("tr:nth-child(12) > td"));
                    Type = shadow_root3.findElement(By.cssSelector("tr:nth-child(13) > td"));
                }
                else {
                    OS = shadow_root3.findElement(By.cssSelector("tr:nth-child(10) > td"));
                    Priority = shadow_root3.findElement(By.cssSelector("tr:nth-child(11) > td"));
                    Type = shadow_root3.findElement(By.cssSelector("tr:nth-child(12) > td"));
                }

                WebElement root4 = driver.findElement(By.cssSelector("#issue > div > div.issue-header-container > mr-issue-header"));
                SearchContext shadow_root4 = root4.getShadowRoot();

                WebElement title = shadow_root4.findElement(By.cssSelector("div.main-text-outer > div > h1"));
                StringTokenizer st = new StringTokenizer(title.getText(),":");



                WebElement reporter = shadow_root4.findElement(By.cssSelector("div.main-text-outer > div > small > mr-user-link"));


                WebElement starttime = shadow_root4.findElement(By.cssSelector("div.main-text-outer > div > small > chops-timestamp"));

                SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy, h:m aa 'GMT+8'", Locale.ENGLISH);
                String t1="";
                try {
                    Date date2 = sdf.parse(starttime.getText());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    t1=simpleDateFormat.format(date2);
                    sdf.format(new Date());
                } catch (ParseException e) {
                }


                WebElement root7 = driver.findElement(By.cssSelector("#issue > div > div.container-issue-content > mr-issue-details > mr-description"));
                SearchContext shadow_root7 = root7.getShadowRoot();

                WebElement root8 = shadow_root7.findElement(By.cssSelector("mr-comment-content"));
                SearchContext shadow_root8 = root8.getShadowRoot();

                int patch=0;
                try {
                    WebElement root9 = shadow_root7.findElement(By.cssSelector("div:nth-child(3) > mr-attachment"));
                    SearchContext shadow_root9 = root9.getShadowRoot();
                    WebElement fn=shadow_root9.findElement(By.cssSelector("div > div.filename"));
                    if(fn.getText().indexOf("patch")!=-1)
                        patch=1;
                } catch (Exception e) {
                    // TODO: handle exception
                }


                int test=0;

                List<WebElement> contents = shadow_root8.findElements(By.className("line"));
                for(int i=0;i<contents.size();i++){
                    //System.out.println(contents.get(i).getText());
                    if(contents.get(i).getText().indexOf("test")!=-1||contents.get(i).getText().indexOf("Test")!=-1||contents.get(i).getText().indexOf("case")!=-1)
                        test=1;
                }


                WebElement root6 = driver.findElement(By.cssSelector("#issue > div > div.container-issue-content > mr-issue-details > mr-comment-list"));
                SearchContext shadow_root6 = root6.getShadowRoot();

                List<WebElement> comments = shadow_root6.findElements(By.cssSelector("mr-comment"));
                int n=comments.size();
                for(int i=0;i<comments.size();i++){
                    if(comments.get(i).getText().indexOf("Deleted")!=-1)
                        n--;
                }

                String t3="";
                String t4="";
                int count = 0;//modified add总和
                String result="";
                if(n!=0){
                    int m=0;
                    for(int i=0;i<comments.size();i++){
                        if(comments.get(i).getText().indexOf(reporter.getText())==-1&&comments.get(i).getText().indexOf("Deleted")==-1){
                            m=i;
                            break;
                        }
                    }

                    String tt=comments.get(m).getText().substring(comments.get(m).getText().indexOf(",")+2,comments.get(m).getText().indexOf("GMT+8")-1);//提取日期字符串

                    SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy, h:m aa", Locale.ENGLISH);

                    try {
                        Date date2 = sdf2.parse(tt);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        t3=simpleDateFormat.format(date2);
                        sdf2.format(new Date());
                    } catch (ParseException e) {
                    }


                    int l=0;
                    for(int i=0;i<comments.size();i++){
                        if(comments.get(i).getText().indexOf("Status: Fixed")!=-1){
                            l=i;
                            break;
                        }
                    }
                    String tt1=comments.get(l).getText().substring(comments.get(l).getText().indexOf(",")+2,comments.get(l).getText().indexOf("GMT+8")-1);//提取日期字符串

                    SimpleDateFormat sdf3 = new SimpleDateFormat("MMM d, yyyy, h:m aa", Locale.ENGLISH);
                    try {
                        Date date2 = sdf3.parse(tt1);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        t4=simpleDateFormat.format(date2);
                        sdf3.format(new Date());
                    } catch (ParseException e) {
                    }


                    int index = 0;//modified add索引
                    String key[]={"[modify]","[add]"};
                    for(int i=0;i<comments.size();i++){
                        for(int j=0;j<key.length;j++){
                            while ((index = comments.get(i).getText().indexOf(key[j], index)) != -1) {
                                index = index + key[j].length();
                                count++;
                            }
                        }

                    }

                    if(count!=0){

                        for(int i=0;i<comments.size();i++){
                            String parent=comments.get(i).getText();
                            String sub1="[modify]";
                            String sub2="[add]";
                            while (parent.indexOf(sub1)!= -1||parent.indexOf(sub1)!= -1) {
                                int ind1=0;int ind2=0;
                                if(parent.indexOf(sub1)!= -1){
                                    ind1=parent.lastIndexOf(sub1);
                                }
                                if(parent.indexOf(sub2)!= -1){
                                    ind2=parent.lastIndexOf(sub2);
                                }
                                if(ind1>ind2){
                                    int ind=ind1;
                                    String r=parent.substring(ind+sub1.length()+1,parent.length());

                                    result=result+r+",";
                                    parent=parent.substring(0,ind);
                                }
                                else {
                                    int ind=ind2;
                                    String r=parent.substring(ind+sub2.length()+1,parent.length());

                                    result=result+r+",";
                                    parent=parent.substring(0,ind);
                                }

                            }



                        }

                    }


                }
                else {
                    continue;
                }

                String value="";
                if(result.isEmpty()){
                    value = k+ "," +"\"" + title.getText().substring(st.nextToken().length()+2).replaceAll("\r|\n", "")+"\""  + ","+"\"" + reporter.getText().replaceAll("\r|\n", "") +"\""+ "," +t1+ ","+"\"" +owner.getText().replaceAll("\r|\n", "")+"\""+ "," +"\""+status.getText().replaceAll("\r|\n", "")+"\""+ "," +t2+ "," +"\""+Components.getText().replaceAll("\r|\n", "")+"\""
                            + ","+"\"" +OS.getText().replaceAll("\r|\n", "")+"\""+ "," +"\""+Priority.getText().replaceAll("\r|\n", "")+"\""+ ","+"\"" +Type.getText().replaceAll("\r|\n", "")+"\""+ "," +String.valueOf(n)+ "," +t3+ "," +t4+ "," +String.valueOf(count)+ "," + ","+String.valueOf(test)+ ","+String.valueOf(patch) ;
                }
                else {
                    value = k+ "," +"\"" + title.getText().substring(st.nextToken().length()+2).replaceAll("\r|\n", "")+"\""  + ","+"\"" + reporter.getText().replaceAll("\r|\n", "") +"\""+ "," +t1+ ","+"\"" +owner.getText().replaceAll("\r|\n", "")+"\""+ "," +"\""+status.getText().replaceAll("\r|\n", "")+"\""+ "," +t2+ "," +"\""+Components.getText().replaceAll("\r|\n", "")+"\""
                            + ","+"\"" +OS.getText().replaceAll("\r|\n", "")+"\""+ "," +"\""+Priority.getText().replaceAll("\r|\n", "")+"\""+ ","+"\"" +Type.getText().replaceAll("\r|\n", "")+"\""+ "," +String.valueOf(n)+ "," +t3+ "," +t4+ "," +String.valueOf(count) + "," +"\"" +result.substring(0,result.length()-1).replaceAll("\r|\n", "")+"\"" + ","+String.valueOf(test)+ ","+String.valueOf(patch);
                }

                csvbw.write(value + "\n");

            } catch (Exception e) {

                csvbw.write("网页解析错误"+ "\n");
                continue;
            }





        }

        csvbw.flush();
        csvbw.close();

    }

}
