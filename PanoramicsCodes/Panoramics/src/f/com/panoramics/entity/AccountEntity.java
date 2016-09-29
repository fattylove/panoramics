package f.com.panoramics.entity;

import java.io.Serializable;

import fatty.library.sqlite.core.ANNOTATION_ID;
import fatty.library.sqlite.core.ANNOTATION_TABLE;

@SuppressWarnings("serial")
@ANNOTATION_TABLE(name = "panoramics_account_table")
public class AccountEntity implements  Serializable {

	@ANNOTATION_ID
	private String accountId;

	private int follows;
	private int liked_by;
	private int followed_by;

	private String followState;

	private String passport;
	private String password;
	private String username;
	private String nickname;
	private String avatar;
	private String website;
	private String bio;
	private String phone;
	private String gender;

	public String getFollowState() {
		return followState;
	}

	public void setFollowState(String followState) {
		this.followState = followState;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public int getFollows() {
		return follows;
	}

	public void setFollows(int follows) {
		this.follows = follows;
	}

	public int getLiked_by() {
		return liked_by;
	}

	public void setLiked_by(int liked_by) {
		this.liked_by = liked_by;
	}

	public int getFollowed_by() {
		return followed_by;
	}

	public void setFollowed_by(int followed_by) {
		this.followed_by = followed_by;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
