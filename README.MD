greenDao Converter自动生成器
可自动生成对应实体的converter类,省略自己重复定义converter工作.

[![](https://jitpack.io/v/TanZhiL/GreenConverter.svg)](https://jitpack.io/#TanZhiL/GreenConverter)
```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
    }
}

  implementation 'com.github.TanZhiL.GreenConverter:greenconverter:x.x'
  annotationProcessor 'com.github.TanZhiL.GreenConverter:greenconverter-compiler:x.x'



@Converter
@ListConverter
public class User {
}



@Entity
public class UserEntity {
    @Id(autoincrement = true)
    private long id;
	//此处一定要带上全包名"com.thomas.GreenConverter"不支持同时出现两个类名相同的实体.
    @Convert(columnType = String.class,converter = com.thomas.GreenConverter.User_Converter.class)
    private User mUser;
    @Convert(columnType = String.class,converter =com.thomas.GreenConverter.User_ListConverter.class)
    private List<User> mUsers;



```


### 致谢
* 感谢所有开源库的大佬
### 问题反馈
欢迎加星，打call https://github.com/TanZhiL/GreenConverter
* email：1071931588@qq.com
### 关于作者
谭志龙
### 开源项目
* 快速切面编程开源库 https://github.com/TanZhiL/OkAspectj
* 高仿喜马拉雅听Android客户端 https://github.com/TanZhiL/Zhumulangma
* 骨架屏弹性块 https://github.com/TanZhiL/SkeletonBlock
* RxPersistence是基于面向对象设计的快速持久化框架 https://github.com/TanZhiL/RxPersistence
* greenDao Converter自动生成器 https://github.com/TanZhiL/GreenConverter

## License

    Copyright 2020 Thomas, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
