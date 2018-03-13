package com.topcom.cms.service.impl;

import com.topcom.cms.base.service.impl.GenericManagerImpl;
import com.topcom.cms.dao.SearchWordDao;
import com.topcom.cms.domain.SearchWord;
import com.topcom.cms.service.SearchWordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 资源访问实现类
 * 
 * @author lism
 * 
 */
@Service(value = "searchWordManager")
@Transactional
public class SearchWordManagerImpl extends GenericManagerImpl<SearchWord, Long>
implements SearchWordManager {

	SearchWordDao searchWordDao;

	@Autowired
	public void setSearchWordDao(SearchWordDao searchWordDao) {
		this.searchWordDao = searchWordDao;
		this.dao = this.searchWordDao;
	}

	@Override
	public List<SearchWord> findByWordAndType(String word, Integer type) {
		return this.searchWordDao.findByWordAndType(word,type);
	}

	@Override
	public Page<SearchWord> findByType(Pageable page, Integer type) {
		return this.searchWordDao.findByType(page,type);
	}

	@Override
	public SearchWord addClickCount(String word, Integer type) {
		SearchWord result = new SearchWord();
		List<SearchWord> searchWords = this.findByWordAndType(word,type);
		if (searchWords!=null&&searchWords.size()>0){
			if (searchWords.size()>1){
				for (int i=1;i<searchWords.size();i++){
					this.delete(searchWords.get(i).getId());
				}
			}
			result = searchWords.get(0);
			long wordCount = result.getWordCount();
			result.setWordCount(++wordCount);
			result.setDateModified(new Date());
		}else {
			result.setType(type);
			result.setWord(word);
			result.setWordCount(1);
		}
		return this.save(result);
	}
}
