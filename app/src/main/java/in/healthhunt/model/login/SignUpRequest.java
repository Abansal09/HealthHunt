package in.healthhunt.model.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishekkumar on 4/26/18.
 */

public class SignUpRequest {

    @SerializedName("email")
    private String mEmail;

    @SerializedName("first_name")
    private String mFirstName;

    @SerializedName("password")
    private String mPassword;

   /* @SerializedName("username")
    private String mUserName;
*/
    @SerializedName("name")
    private String mName;

    @SerializedName("gender")
    private String mGender;

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    /*public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }*/

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String gender) {
        this.mGender = gender;
    }
}