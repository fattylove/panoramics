package f.com.panoramics.entity;

import java.io.Serializable;

import fatty.library.sqlite.core.ANNOTATION_ID;
import fatty.library.sqlite.core.ANNOTATION_TABLE;

@SuppressWarnings("serial")
@ANNOTATION_TABLE(name = "panoramics_media_table")
public class MediaEntity implements  Serializable {

	@ANNOTATION_ID
	private int id;

	private String mediaId;

	private int like;
	private int likeState;

	private long createTime;
	private long likedTime;

	private double lat;
	private double lng;
	private String accountId;
	private String nickname;
	private String avatar;
	private String tags;
	private String location;

	private int shared_cnt;
	private int browsed_cnt;
	private String type;
	private String standard_resolution;
	private String low_resolution;
	private String thumbnail;
	private int groupNum;
	private String ids;
	private int stateTag;

	private long p_time;

	public long getLikedTime() {
		return likedTime;
	}

	public void setLikedTime(long likedTime) {
		this.likedTime = likedTime;
	}

	public int getLikeState() {
		return likeState;
	}

	public void setLikeState(int likeState) {
		this.likeState = likeState;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getP_time() {
		return p_time;
	}

	public void setP_time(long p_time) {
		this.p_time = p_time;
	}

	public int getShared_cnt() {
		return shared_cnt;
	}

	public void setShared_cnt(int shared_cnt) {
		this.shared_cnt = shared_cnt;
	}

	public int getBrowsed_cnt() {
		return browsed_cnt;
	}

	public void setBrowsed_cnt(int browsed_cnt) {
		this.browsed_cnt = browsed_cnt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStandard_resolution() {
		return standard_resolution;
	}

	public void setStandard_resolution(String standard_resolution) {
		this.standard_resolution = standard_resolution;
	}

	public String getLow_resolution() {
		return low_resolution;
	}

	public void setLow_resolution(String low_resolution) {
		this.low_resolution = low_resolution;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public int getStateTag() {
		return stateTag;
	}

	public void setStateTag(int stateTag) {
		this.stateTag = stateTag;
	}

}
