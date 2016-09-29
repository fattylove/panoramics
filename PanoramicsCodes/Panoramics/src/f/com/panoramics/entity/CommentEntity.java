package f.com.panoramics.entity;

import java.io.Serializable;

import fatty.library.sqlite.core.ANNOTATION_ID;
import fatty.library.sqlite.core.ANNOTATION_TABLE;

/**
 * 
 * @author Fatty
 * 
 *         某个人某张图片的评论
 * 
 */
@SuppressWarnings("serial")
@ANNOTATION_TABLE(name = "panoramics_comment_table")
public class CommentEntity implements  Serializable {

	@ANNOTATION_ID
	private String commentId;

	private String accountId;
	private String text;
	private String nickname;
	private String username;
	private long createTime;
	private String avatar;

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
