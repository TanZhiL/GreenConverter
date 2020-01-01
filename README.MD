greenDao Converter自动生成器

[![](https://jitpack.io/v/TanZhiL/GreenConverter.svg)](https://jitpack.io/#TanZhiL/GreenConverter)

  implementation 'com.github.TanZhiL.GreenConverter:greenconverter:1.1'
  annotationProcessor 'com.github.TanZhiL.GreenConverter:greenconverter-compiler:1.1'



@Converter
@ListConverter
public class User {
}



@Entity
public class UserEntity {
    @Id(autoincrement = true)
    private long id;
	//此处一定要带上全包名
    @Convert(columnType = String.class,converter = com.thomas.greenconverter.User_Converter.Converter.class)
    private User mUser;
    @Convert(columnType = String.class,converter =com.thomas.greenconverter.User_ListConverter.ListConverter.class)
    private List<User> mUsers;