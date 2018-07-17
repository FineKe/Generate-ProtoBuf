此插件主要是为了方便编译proto文件 ，目前只支持了 go语言Grpc 微服务的生成

###使用：
* 1.前提:
    
    
    proto 工具下载
    
    
    1、下载 idl 代码生成工具
    
    
    https://repo1.maven.org/maven2/com/google/protobuf/protoc/ protoc 放在环境变量的 path 中
    
    
    2、下载生成 go 代码插件
    
    
    go get -u github.com/golang/protobuf/protoc-gen-go
    
    3.grpc 包下载
    
    go get -u google.golang.org/grpc
    
* 2.选中.proto文件，右键Generate ProtoBuf 即可
