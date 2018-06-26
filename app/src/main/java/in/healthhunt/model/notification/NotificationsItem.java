package in.healthhunt.model.notification;

public class NotificationsItem{
	private String date;
	private boolean is_viewed;
	private String user_name;
	private String post_slug;
	private int notification_id;
	private String video_thumbnail_image;
	private String event_type;
	private int post_id;
	private int user_id;
	private String user_url;
	private int resource_id;
	private String post_type;
	private String category;
	private String event_time;
	private String post_image_url;

	public String getPost_image_url() {
		return post_image_url;
	}

	public void setPost_image_url(String post_image_url) {
		this.post_image_url = post_image_url;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setIsViewed(boolean isViewed){
		this.is_viewed = isViewed;
	}

	public boolean IsViewed(){
		return is_viewed;
	}

	public void setUser_name(String user_name){
		this.user_name = user_name;
	}

	public String getUser_name(){
		return user_name;
	}

	public void setPost_slug(String post_slug){
		this.post_slug = post_slug;
	}

	public String getPost_slug(){
		return post_slug;
	}

	public void setNotification_id(int notification_id){
		this.notification_id = notification_id;
	}

	public int getNotification_id(){
		return notification_id;
	}

	public void setVideo_thumbnail_image(String video_thumbnail_image){
		this.video_thumbnail_image = video_thumbnail_image;
	}

	public String getVideo_thumbnail_image(){
		return video_thumbnail_image;
	}

	public void setEvent_type(String event_type){
		this.event_type = event_type;
	}

	public String getEvent_type(){
		return event_type;
	}

	public void setPost_id(int post_id){
		this.post_id = post_id;
	}

	public int getPost_id(){
		return post_id;
	}

	public void setUser_id(int user_id){
		this.user_id = user_id;
	}

	public int getUser_id(){
		return user_id;
	}

	public void setUser_url(String user_url){
		this.user_url = user_url;
	}

	public String getUser_url(){
		return user_url;
	}

	public void setResource_id(int resource_id){
		this.resource_id = resource_id;
	}

	public int getResource_id(){
		return resource_id;
	}

	public void setPost_type(String post_type){
		this.post_type = post_type;
	}

	public String getPost_type(){
		return post_type;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return category;
	}

	public void setEvent_time(String event_time){
		this.event_time = event_time;
	}

	public String getEvent_time(){
		return event_time;
	}

	@Override
 	public String toString(){
		return 
			"NotificationsItem{" + 
			"date = '" + date + '\'' + 
			",is_viewed = '" + is_viewed + '\'' +
			",user_name = '" + user_name + '\'' +
			",post_slug = '" + post_slug + '\'' +
			",notification_id = '" + notification_id + '\'' +
			",video_thumbnail_image = '" + video_thumbnail_image + '\'' +
			",event_type = '" + event_type + '\'' +
			",post_id = '" + post_id + '\'' +
			",user_id = '" + user_id + '\'' +
			",user_url = '" + user_url + '\'' +
			",resource_id = '" + resource_id + '\'' +
			",post_type = '" + post_type + '\'' +
			",category = '" + category + '\'' + 
			",event_time = '" + event_time + '\'' +
			",post_image_url = '" + post_image_url + '\'' +
			"}";
		}
}
