package in.healthhunt.model.login;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Table(name = "Users")
public class User extends Model{

	//@Column(name = "capabilities")
	//private Capabilities capabilities;

	@Column(name = "user_image")
	private String user_image;

	//@Column(name = "roles")
	private List<String> roles;

	@Column(name = "link")
	private String link;

	@Column(name = "last_name")
	private String last_name;

	@Column(name = "description")
	private String description;

	@Column(name = "url")
	private String url;
	//private Collections collections;

	//@Column(name = "popularity")
	private Popularity popularity;

	@Column(name = "name")
	private String name;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "user_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	@SerializedName("id")
	private String user_id;

	@Column(name = "registered_date")
	private String registered_date;

	//@Column(name = "extraCapabilities")
	//private ExtraCapabilities extraCapabilities;

	@Column(name = "first_name")
	private String first_name;

	@Column(name = "email")
	private String email;

	@Column(name = "slug")
	private String slug;

	@Column(name = "username")
	private String username;

/*
	@Column(name = "bio")   //not coming from  server
	private String bio;
*/

	@Column(name = "tags")  //not coming from  server
	private String tagList;

	@Column(name = "currentLogin")  //not coming from  server
	private boolean currentLogin;

	@Column(name = "continueList")  //not coming from  server
	private String continueList;


	/*public void setCapabilities(Capabilities capabilities){
		this.capabilities = capabilities;
	}

	public Capabilities getCapabilities(){
		return capabilities;
	}
*/

	public String getContinueList() {
		return continueList;
	}

	public void setContinueList(String continueList) {
		this.continueList = continueList;
	}

	public boolean isCurrentLogin() {
		return currentLogin;
	}

	public void setCurrentLogin(boolean currentLogin) {
		this.currentLogin = currentLogin;
	}

	public String getTagList() {
		return tagList;
	}

	public void setTagList(String tagList) {
		this.tagList = tagList;
	}

	/*public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}
*/
	public void setUser_image(String user_image){
		this.user_image = user_image;
	}

	public String getUser_image(){
		return user_image;
	}

	public void setRoles(List<String> roles){
		this.roles = roles;
	}

	public List<String> getRoles(){
		return roles;
	}

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}

	public void setLast_name(String last_name){
		this.last_name = last_name;
	}

	public String getLast_name(){
		return last_name;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	/*public void setCollections(Collections collections){
		this.collections = collections;
	}

	public Collections getCollections(){
		return collections;
	}
*/
	public void setPopularity(Popularity popularity){
		this.popularity = popularity;
	}

	public Popularity getPopularity(){
		return popularity;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setNickname(String nickname){
		this.nickname = nickname;
	}

	public String getNickname(){
		return nickname;
	}

	public void setUserId(String id){
		this.user_id = id;
	}

	public String getUserId(){
		return user_id;
	}

	public void setRegistered_date(String registered_date){
		this.registered_date = registered_date;
	}

	public String getRegistered_date(){
		return registered_date;
	}

	/*public void setExtraCapabilities(ExtraCapabilities extraCapabilities){
		this.extraCapabilities = extraCapabilities;
	}

	public ExtraCapabilities getExtraCapabilities(){
		return extraCapabilities;
	}*/

	public void setFirst_name(String first_name){
		this.first_name = first_name;
	}

	public String getFirst_name(){
		return first_name;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setSlug(String slug){
		this.slug = slug;
	}

	public String getSlug(){
		return slug;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public static User getUser(String userId){
		return new Select().from(User.class).where("user_id = ?" , userId).executeSingle();
	}

	public static User getCurrentUser(){
		return new Select().from(User.class).where("currentLogin = ?" , true).executeSingle();
	}

	public static List<User> getAllUser(){
		return new Select().from(User.class).execute();
	}

	public static void removeUser(String user_id){
		new Delete().from(User.class).where("user_id = ?" , user_id).execute();
	}



	@Override
	public String toString(){
		return
				"User{" +
						//"capabilities = '" + capabilities + '\'' +
						",user_image = '" + user_image + '\'' +
						",roles = '" + roles + '\'' +
						",link = '" + link + '\'' +
						",last_name = '" + last_name + '\'' +
						",description = '" + description + '\'' +
						",url = '" + url + '\'' +
						//'" + collections + '\'' +
						",popularity = '" + popularity + '\'' +
						",name = '" + name + '\'' +
						",nickname = '" + nickname + '\'' +
						",userid = '" + user_id + '\'' +
						",registered_date = '" + registered_date + '\'' +
						//",extra_capabilities = '" + extraCapabilities + '\'' +
						",first_name = '" + first_name + '\'' +
						",email = '" + email + '\'' +
						",slug = '" + slug + '\'' +
						",username = '" + username + '\'' +
						"}";
	}
}