package f.com.panoramics.service.dbservice;

import android.content.Context;
import android.text.TextUtils;
import f.com.panoramics.entity.AccountEntity;
import fatty.library.sqlite.core.SQLService;

/**
 * 
 * @author Fatty
 *
 */
public class AccountDBService  {

	private final SQLService service;

	/**
	 * 
	 * @param context
	 */
	public AccountDBService(Context context) {
		service = SQLService.create(context, true);
	}

	/**
	 * 存储Media数据
	 * 
	 * @param accountEntity
	 */
	public void saveAccountEntity(AccountEntity  accountEntity) {
		if(accountEntity != null){
			AccountEntity findAccountEntity = findByAccountId(accountEntity.getAccountId());
			if(findAccountEntity != null){
				if(!TextUtils.equals(accountEntity.getAccountId(), findAccountEntity.getAccountId())){
					save(accountEntity);
				}else{
					update(accountEntity);
				}
			}else{
				save(accountEntity);
			}
		}
	}
	
	/**
	 * 根据accountId获取AccountEntity
	 * 
	 * @param accountId
	 * @return
	 */
	public AccountEntity findByAccountId(String accountId) {
		return service.findById(accountId, AccountEntity.class);
	}

	/**
	 * 保存accountEntity
	 * 
	 * @param accountEntity
	 */
	private void save(AccountEntity accountEntity){
		service.save(accountEntity);
	}

	/**
	 * 更新accountEntity
	 * 
	 * @param accountEntity
	 */
	private void update(AccountEntity accountEntity) {
		service.update(accountEntity);
	}

}
