#!/bin/bash

#指定使用source xxx.sh的执行方式
if [ $0 != '-bash' ] && [ $0 != 'bash' ];then
	echo "请使用source xxx.sh方式运行脚本"
	exit 1
fi

#获取包名
pgk_name=$(ls | grep njht_)
#获取文件夹名称
# project_path=$(cd `dirname $0`; pwd)
project_path=$(pwd)
dir_name="${project_path##*/}"
#获取版本号
version=${pgk_name#*${dir_name}_}
version=${version%*.jar}

#判断是否存在该镜像（dir_name:version）
is_exist=$(docker images | grep $dir_name | wc -l)
#如果存在，删除该镜像
if [ $is_exist == 1 ];then
	#判断是否存在容器
	is_container_exist=$(docker ps -a | grep $dir_name | wc -l)
	if [ $is_container_exist == 1 ];then
		docker stop $dir_name
		docker rm $dir_name
	fi
	img_id=$(docker images | grep $dir_name | awk '{print $3}')
	docker rmi $img_id
fi

#构建镜像
docker build -t $dir_name:$version .
service_name=${dir_name#*njht_}
#将版本号存入~/.bash_profile中，供docker-compose配置文件使用
ver_name=${service_name^^}_VERSION
is_env_exist=$(grep $ver_name ~/.bash_profile | wc -l)
#如果该变量存在，则修改，否则新建
if [ $is_env_exist == 1 ];then
	sed -i "s/\(export ${ver_name}=\).*/\1${version}/g" ~/.bash_profile
else
	echo "export ${ver_name}=${version}" >> ~/.bash_profile
fi
#重新加载环境变量
source ~/.bash_profile
