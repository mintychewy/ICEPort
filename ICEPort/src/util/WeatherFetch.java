package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFrame;
 
 
public class WeatherFetch extends Thread{
        static String a;
        int REFRESH_INTERVAL=1;
        boolean error=false;
        JFrame weather;
        public WeatherFetch(){
        	weather=new JFrame();
        	weather.setSize(900,750);
        	
        }
       
        //static String currentstate;
        static String[]weathers={"Sunny","Cloudy","Raining","Snowing"};
        public boolean isReachable(String address) {
                Socket socket = null;
                boolean reachable = false;
                try {
                        socket = new Socket();
                        socket.connect(new InetSocketAddress(address, 80), 1000);
                        reachable = true;
                }
                catch (Exception e) {
                }
                finally {
                        if (socket != null)
                                try {
                                        socket.close();
                                }
                        catch (IOException e) {}
                }
    return reachable;
        }
        public void setInterval(int time){
                if(time<=10){
                REFRESH_INTERVAL=time;
                }else{
                        error=true;
                }
        }
       
        public int findIndex(String currentstate){
                int index = 0;
                if(currentstate.equals(weathers[0])){
                        index=0;
                }
                else if(currentstate.equals(weathers[1])){
                        index=1;
                }else if(currentstate.equals(weathers[2])){
                        index=2;
                }
                else if(currentstate.equals(weathers[3])){
                        index=3;
               
                }
                return index;
        }
        public void chageState(){
               
        }
        public String getWeather(){
       
                String w =a.substring(44,54);
               
               
                return w.substring(0,w.lastIndexOf(",")-1);
               
               
        }
        public String getTimeStamp(){
                //String ts = a.substring(65, 75);
                int c=0;
                int index=0;
                int ja=0;
                for(index=0; index<100;index++){
                        if(a.substring(index,index+1).equals(":")){
                       
                                c++;
                       
                        }
                        if(c==4){
                                ja=index;
                        }
                }
                String ts=a.substring(ja+2, ja+12);
               
                return ts;
        }
       
        public String ProcessReq(String req) throws MalformedURLException, IOException{
 
                String result = "";
 
                if (req.indexOf(' ') == -1){
                        URL a = new URL("http://iceworld.sls-atl.com/api/&cmd="+req);
                URLConnection yc = a.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                String inputLine;
 
                while ((inputLine = in.readLine()) != null) {
                    result += inputLine+"\n";
                }
                in.close();
                }else{
                        result = ProcessReq(req.substring(0,req.indexOf(' ')),req.substring(req.indexOf(' ')+1,req.length()));
                }
                return result;
        }
        public String ProcessReq(String req1, String req2) throws MalformedURLException, IOException{
                String result="";
                String s = "";
                if(req1.equalsIgnoreCase("actions")){
                        s=req1+"&from="+req2;
                } else if(req1.equalsIgnoreCase("gresources")){
                        s=req1+"&uid="+req2;
                } else if(req1.equalsIgnoreCase("gurl")){
                        s=req1+"&gid="+req2;
                }
                URL a = new URL("http://iceworld.sls-atl.com/api/&cmd="+s);
        URLConnection yc = a.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            result += inputLine+"\n";
        }
        in.close();
                return result;
        }
        public void run(){
                try {
                        while(true){
                        	
                        	a = ProcessReq("states");
                       // System.out.println(a);
                       
                        WeatherFetch b = new WeatherFetch();
                        String currentstate =b.getWeather();
                        System.out.println(currentstate);
                       WeatherAnimator x=new WeatherAnimator(currentstate);
                        weather.setContentPane(x.getWeatherPanel());
                        weather.setVisible(true);
                       
               
                        System.out.println(b.getTimeStamp());
                        int indexc= findIndex(currentstate);
                      //  System.out.println(indexc);
                        sleep(REFRESH_INTERVAL*1000);
                        }
                       
                       
                } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
               
        }
}