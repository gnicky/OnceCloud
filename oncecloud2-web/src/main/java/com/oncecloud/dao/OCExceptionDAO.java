package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import com.oncecloud.entity.OCHException;

public interface OCExceptionDAO {

	public abstract void save(OCHException exc);

	public abstract void delete(Date startTime, Date endTime);

	public abstract List<OCHException> search(int userId, Date startTime,
			Date endTime);

}
