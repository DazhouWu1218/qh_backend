package com.njht.webyun.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FindProcessUtils {


    public static void main(String []args){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long pidTime;
        Long curTiem;
        Long timeDIff;
        String processName="postman.exe";
        System.out.println(findProcessByName(processName));
        Map<String, List<String>> map =findProcessByName(processName);
        if (map.size()!=0 && !map.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                List<String> mapValue = entry.getValue();
                //得到拼接好的时间格式的字符串
                String time = mapValue.get(1);
                try {
                    Date date = new Date();
                    pidTime = format.parse(time).getTime();
                    curTiem = date.getTime();
                    timeDIff = (curTiem - pidTime) / 1000 / 60;
                    //进程存在时间超过临界值，强行终止进程，单位：分钟
                    if (timeDIff > 60) {
                        //关闭进程
//                        Runtime.getRuntime().exec("taskkill /pid " + mapKey + "  -t  -f");
                    }else {
                        //不超过时间
                        System.out.println("进程运行时间小于临界值");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
        }else {
            System.out.println(processName+"任务进程不存在");
        }

    }
    public static Map findProcessByName(String processName) {
        String s1="";
        //存储进程的相关信息
        Map<String,List<String>> map=new HashMap<>();
        String[] s;
        BufferedReader br = null;
        try {
            //获得windods下程序所有进程的pid
            Process proc = Runtime.getRuntime().exec("wmic process where name=\""+processName+"\" get processid");
            br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = br.readLine();
            if (line.isEmpty()){
                return map;
            }
            while (line!=null) {
                s1+=line;
                line = br.readLine();
            }
            s=s1.substring(9).trim().replaceAll("\\s{2,}"," ").split(" ");
            for (int i=0;i<s.length;i++){
                map.put(s[i],findProcessById(s[i]));
                System.out.println(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return map;
    }

    public static List findProcessById(String processId) {
        String s1="";
        String s2="";
        //存放所有进程的pid
        List<String> list=new ArrayList<>();
        BufferedReader br1 = null;
        BufferedReader br2 =null;
        try {
            //根据进程的pid查询出进程任务的创建时间
            Process proc1 = Runtime.getRuntime().exec("wmic process where processid=\""+processId+"\" get CreationDate");
            //根据进程的pid查询出进程任务的命令行
            Process proc2 = Runtime.getRuntime().exec("wmic process where processid=\""+processId+"\" get Commandline");
            br1 = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
            //Windows下dos窗口默认采用"GBK"编码方式
            br2 = new BufferedReader(new InputStreamReader(proc2.getInputStream(),"GBK"));
            String line1 = br1.readLine();
            String line2 = br2.readLine();
            while (line1!=null) {
                s1+=line1;
                line1 = br1.readLine();
            }
            while (line2!=null) {
                s2+=line2;
                line2 = br2.readLine();
            }
            //得到类似20211221202033格式的字符串
            s1=s1.substring(12).trim().substring(0,14);
            String year=s1.substring(0,4);
            String month=s1.substring(4,6);
            String day=s1.substring(6,8);
            String hour=s1.substring(8,10);
            String minute=s1.substring(10,12);
            String second=s1.substring(12,14);
            //拼接成时间的格式,方便之后做有关时间的处理
            String time=year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;

            s2=s2.substring(11).trim();
            list.add(s2);
            list.add(time);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br1 != null) {
                try {
                    br1.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (br2 != null) {
                try {
                    br2.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return list;
    }
}
