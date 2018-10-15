package com.harry.bpm.jpa.repository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.harry.bpm.bean.BpmProcdms;
import com.harry.bpm.bean.pk.BpmProcdmsPK;
import com.harry.bpm.exception.BpmException;
import com.harry.bpm.exception.BpmMessage;
import com.harry.bpm.jpa.service.BpmProcdmsService;

@Repository("bpmProcdmsService")
public class BpmProcdmsRepository extends BaseRepository<BpmProcdms> implements BpmProcdmsService {

	@Override
	@Transactional
	public void delete(BpmProcdms t) {
		if (null != t) {
			try {
				BpmProcdms reference = em.getReference(BpmProcdms.class,
						new BpmProcdmsPK(t.getIdkey().getProcType(), t.getIdkey().getDmsKey()));
				if (null!=reference) {
					this.em.remove(reference);
				}
			} catch (EntityNotFoundException e) {
				throw new BpmException(BpmMessage.W0001,e);
			}finally {
				em.close();
			}
		}
	}

}
