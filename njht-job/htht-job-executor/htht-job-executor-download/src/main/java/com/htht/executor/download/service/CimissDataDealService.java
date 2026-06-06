package com.htht.executor.download.service;

import com.htht.executor.cimiss.bean.ResultBean;
import com.htht.job.core.entity.paramtemplate.CimissDownParam;

public interface CimissDataDealService {

	void dealStationData(CimissDownParam cimissParam, ResultBean resultBean);
}
