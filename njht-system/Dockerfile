# 环境
FROM java8:1.1
# 将本地文件tmp挂载到容器
VOLUME /tmp
#创建picture目录
# 拷贝jar
ADD njht_system*.jar /njht_system.jar
#拷贝第三方库
#ADD lib /lib
#拷贝配置文件
#ADD config /config
#配置环境变量
# 设置暴露的端口号
#EXPOSE 25151
#设置中文
ENV LANG zh_CN.UTF8
ENV LANGUAGE zh_CN:en
CMD ["ldconfig"]

ENTRYPOINT ["java","-Dfile.encoding=utf-8","-jar","/njht_system.jar"]