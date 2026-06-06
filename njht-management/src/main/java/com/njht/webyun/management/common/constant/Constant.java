package com.njht.webyun.management.common.constant;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author daiguojun
 */
public class Constant {

	public static  Map<String,String>  map1 = new HashMap<>(1);
	static {
		map1.put("title", "title");
	}

	public static  Map<String,String>  thematicMap = new HashMap<>(1);
	static {
		thematicMap.put("tif", "栅格");
		thematicMap.put("shp", "矢量");
	}

	public static  Map<String,String>  gribMap = new HashMap<>(1);
	static {
		gribMap.put("0800", "08时");
		gribMap.put("2000", "20时");
		gribMap.put("3", "未来3天");
		gribMap.put("7", "未来7天");
		gribMap.put("10", "未来10天");
		gribMap.put("36", "36时");
		gribMap.put("72", "72时");
		gribMap.put("240", "240时");
		gribMap.put("SPCC", "省级网格预报产品");
		gribMap.put("SCMOC", "国家级网格预报指导产品");
	}
	public static  Map<String,String>  gribTimeResolutionMap = new HashMap<>(1);
	static {
		gribTimeResolutionMap.put("1", "逐1时");
		gribTimeResolutionMap.put("3", "逐3时");
		gribTimeResolutionMap.put("12", "逐12时");
		gribTimeResolutionMap.put("24", "逐24时");
	}
	public static  Map<String,String>  ncMap = new HashMap<>(1);
	static {
		ncMap.put("RT", "实时");
		ncMap.put("NRT", "近实时");
		ncMap.put("DAY", "逐24时");
		ncMap.put("HOR", "逐1时");
	}
	public static  Map<String,String>  hCldasMap = new HashMap<>(1);
	static {
		hCldasMap.put("RT", "实时");
		hCldasMap.put("NRT", "近实时");
		hCldasMap.put("DAY", "逐日");
		hCldasMap.put("HOR", "逐日");
	}

	public static  Map<String,String>  statisticMap = new HashMap<>(1);
	static {
		statisticMap.put("businessProduct", "businessStatisticsService");
		statisticMap.put("GridData", "gridStatisticService");
		statisticMap.put("thematicData", "thematicStatisticsService");
		statisticMap.put("CLDASData", "cldasDataService");
	}
	/**
	 * 通过周期对应期次的信息
	 */
	public static  Map<String,List<String>> cycleIssueMap = new HashMap<>(1);
	static {
		List<String> list = new ArrayList<>();
		list.add("yyyyMMdd1200");
		list.add("yyyyMMdd1300");
		list.add("yyyyMMdd1400");
		cycleIssueMap.put("COOH",list);

		List<String> coodList = new ArrayList<>();
		coodList.add("yyyyMMdd0000");
		cycleIssueMap.put("COOD",coodList);
		cycleIssueMap.put("COED",coodList);

		List<String> coamList = new ArrayList<>();
		coamList.add("yyyyMM010000");
		cycleIssueMap.put("COAM",coamList);

		List<String> coay = new ArrayList<>();
		coay.add("yyyy01010000");
		cycleIssueMap.put("COAY",coay);

		List<String> coaqList = new ArrayList<>();
		coamList.add("yyyyMM010000");
		cycleIssueMap.put("COAQ",coaqList);

	}

	/**
	 * 服务产品
	 */
	public static final String SEVICEPRODUCT = "service-product";
	/**
	 业务产品
	 */
	public static final String BUSINESSPRODUCT = "business-product";

	/**
	 * 专题数据
	 */
	public static final String THEMATICDATA = "thematic-data";
	/**
	 * 基础数据
	 */
	public static final String BASICDATA = "basic-data";
	/**
	 * 卫星数据
	 */
	public static final String SATELLITEDATA = "satellite-data";

	/**
	 * 卫星数据下的专题数据
	 */
	public static final String SATELLITETHEMATICDATA = "satellite-thematic-data";
	/**
	 * 站点数据
	 */
	public static final String SITEDATA = "site-data";
	/**
	 * 格点数据
	 */
	public static final String GRIDDATA= "grid-data";

	/**
	 * 专家知识
	 */
	public static final String EXPERTKONWLEDGE = "expert-knowledge";

	/**
	 模型算法
	 */
	public static final String MODELALGRITHM = "model-algorithm";

	public static Map<String, String> baseDataInfoMap = new HashMap<String, String>();
	static
	{
		/**种植结构信息*/
		baseDataInfoMap.put("htht_ach_plant_structure","plantStructure");
		/**养殖结构信息*/
		baseDataInfoMap.put("htht_ach_breed_structure","breedStructure");
		/**主要作物信息*/
		baseDataInfoMap.put("htht_ach_main_crop","mainCrop");
		/**主要农气指标*/
		baseDataInfoMap.put("htht_ach_main_argindex","mainArgIndex");
		/**新型经营主体*/
		baseDataInfoMap.put("htht_ach_new_business","newBusiness");
		/**农机手*/
		baseDataInfoMap.put("htht_ach_arg_mach_operator","machOperator");
		/**农业专家*/
		baseDataInfoMap.put("htht_ach_arg_expert","argExpert");
		/**田块信息*/
		baseDataInfoMap.put("htht_ach_land_info","landInfo");
		/**直通式服务*/
		baseDataInfoMap.put("htht_ach_service_obj","serviceObj");
		/**三区信息*/
		baseDataInfoMap.put("htht_ach_sanqu","sanQuInfo");
		/**三园信息*/
		baseDataInfoMap.put("htht_ach_sanyuan","sanYuanInfo");
		/**主要大宗作物以及产量*/
		baseDataInfoMap.put("htht_ach_yield_count","yieldCountInfo");
		/**作物生长发育期*/
		baseDataInfoMap.put("htht_ach_agme_c01_ele","cropGrowth");
		/**产量因素表*/
		baseDataInfoMap.put("htht_ach_agme_c04_ele","yieldFactors");
		/**产量结构表*/
		baseDataInfoMap.put("htht_ach_agme_c05_ele","outputStructure");
		/**关键农事活动表*/
		baseDataInfoMap.put("htht_ach_agme_c06_ele","agriculturalActivities");
		/**县产量水平表*/
		baseDataInfoMap.put("htht_ach_agme_c07_ele","productLevel");
	}

	/**redis 中存放的作物，发育期,项目名称编码对照信息*/
	public static final String CROPCODE = "cropsCode123456";
	public static final String MATURITYCODE = "maturityCode123456";
	public static final String PROJECTCODE = "projectCode123456";
	/**关键农事活动*/
	public static final String KFA= "KeyFarmingActivities";

	/** 格点数据筛选要素*/
	public static final String dataClf = "dataClf";
	public static final String forecastTime = "forecastTime";
	public static final String forecastAging = "forecastAging";
	public static final String timeResolution = "timeResolution";

}

