package com.oncecloud.dao;

import java.util.Date;
import java.util.List;

import com.oncecloud.entity.FeeEip;
import com.oncecloud.entity.FeeImage;
import com.oncecloud.entity.FeeSnapshot;
import com.oncecloud.entity.FeeVM;
import com.oncecloud.entity.FeeVolume;

public interface FeeDAO {

	public abstract boolean abandonEip(Date endDate, String eipUuid);

//	public abstract int countAllFeeEipList(String search, int uid);
//
//	public abstract int countAllFeeImageList(String search, int uid);
//
//	public abstract int countAllFeeSnapshotList(String search, int uid);
//
//	public abstract int countAllFeeVMList(String search, int uid);
//
//	public abstract int countAllFeeVolumeList(String search, int uid);
//
//	public abstract int countFeeEipDetailList(int uid, Date startMonth,
//			Date endMonth);
//
//	public abstract int countFeeEipDetailList(int uid, String uuid);
//
//	public abstract int countFeeImageDetailList(int uid, Date startMonth,
//			Date endMonth);
//
//	public abstract int countFeeImageDetailList(int uid, String uuid);
//
//	public abstract int countFeeSnapshotDetailList(int uid, Date startMonth,
//			Date endMonth);
//
//	public abstract int countFeeSnapshotDetailList(int uid, String uuid);
//
//	public abstract int countFeeVMDetailList(int uid, Date startMonth,
//			Date endMonth);
//
//	public abstract int countFeeVMDetailList(int uid, String uuid);
//
//	public abstract int countFeeVolumeDetailList(int uid, Date startMonth,
//			Date endMonth);
//
//	public abstract int countFeeVolumeDetailList(int uid, String uuid);

	public abstract boolean deleteSnapshot(Date endDate, String vmUuid);

	public abstract boolean deleteVolume(Date endDate, String volumeUuid);

	public abstract boolean destoryVM(Date endDate, String vmUuid);

//	public abstract double getEipTotalFee(int uid);
//
//	public abstract List<FeeEip> getFeeEipDetailList(int page, int limit,
//			String search, int uid, Date startMonth, Date endMonth);
//
//	public abstract List<FeeEip> getFeeEipDetailList(int page, int limit,
//			String search, int uid, String uuid);
//
//	public abstract List<FeeImage> getFeeImageDetailList(int page, int limit,
//			String search, int uid, Date startMonth, Date endMonth);
//
//	public abstract List<FeeImage> getFeeImageDetailList(int page, int limit,
//			String search, int uid, String uuid);
//
//	public abstract List<FeeSnapshot> getFeeSnapshotDetailList(int page,
//			int limit, String search, int uid, Date startMonth, Date endMonth);
//
//	public abstract List<FeeSnapshot> getFeeSnapshotDetailList(int page,
//			int limit, String search, int uid, String uuid);
//
//	public abstract List<FeeVM> getFeeVMDetailList(int page, int limit,
//			String search, int uid, Date startMonth, Date endMonth);
//
//	public abstract List<FeeVM> getFeeVMDetailList(int page, int limit,
//			String search, int uid, String uuid);
//
//	public abstract List<FeeVolume> getFeeVolumeDetailList(int page, int limit,
//			String search, int uid, Date startMonth, Date endMonth);
//
//	public abstract List<FeeVolume> getFeeVolumeDetailList(int page, int limit,
//			String search, int uid, String uuid);
//
//	public abstract double getImageTotalFee(int uid);
//
//	public abstract List<FeeEip> getOnePageFeeEipList(int page, int limit,
//			String search, int uid);
//
//	public abstract List<FeeImage> getOnePageFeeImageList(int page, int limit,
//			String search, int uid);
//
//	public abstract List<FeeSnapshot> getOnePageFeeSnapshotList(int page,
//			int limit, String search, int uid);
//
//	public abstract List<FeeVM> getOnePageFeeVMList(int page, int limit,
//			String search, int uid);
//
//	public abstract List<FeeVolume> getOnePageFeeVolumeList(int page,
//			int limit, String search, int uid);
//
//	public abstract double getSnapshotTotalFee(int uid);
//
//	public abstract double getVmTotalFee(int uid);
//
//	public abstract double getVolumeTotalFee(int uid);

	public abstract boolean insertFeeEip(Integer eipUID, Date startDate,
			Date endDate, Double eipPrice, Integer eipState, String eipUuid,
			String eipName);

	public abstract boolean insertFeeSnapshot(Integer snapshotUID,
			Date startDate, Date endDate, Double snapshotPrice,
			Integer snapshotState, String vmUuid, String vmName);

	public abstract boolean insertFeeVM(Integer vmUID, Date startDate,
			Date endDate, Double vmPrice, Integer vmState, String vmUuid,
			String vmName);

	public abstract boolean insertFeeVolume(Integer volumeUID, Date startDate,
			Date endDate, Double volumePrice, Integer volumeState,
			String volumeUuid, String volumeName);

//	public abstract boolean updateAliveEipEndDate(Date nowDate);
//
//	public abstract boolean updateAliveImageEndDate(Date nowDate);
//
//	public abstract boolean updateAliveSnapshotEndDate(Date nowDate);
//
//	public abstract boolean updateAliveVmEndDate(Date nowDate);
//
//	public abstract boolean updateAliveVolumeEndDate(Date nowDate);
//
//	/**
//	 * @param eipuuid
//	 * @param newName
//	 * @author xpx 2014-7-8
//	 */
//	public abstract boolean updateEipName(String eipuuid, String newName);
//
//	/**
//	 * @param imageuuid
//	 * @param newName
//	 * @author xpx 2014-7-8
//	 */
//	public abstract boolean updateImageName(String imageuuid, String newName);
//
//	/**
//	 * @param vmUuid
//	 * @param newName
//	 * @author xpx 2014-7-8
//	 */
//	public abstract boolean updateSnapshotVMName(String vmUuid, String newName);
//
//	/**
//	 * @param vmuuid
//	 * @param newName
//	 * @author xpx 2014-7-8
//	 */
//	public abstract boolean updateVmName(String vmuuid, String newName);
//
//	/**
//	 * @param volumeuuid
//	 * @param newName
//	 * @author xpx 2014-7-8
//	 */
//	public abstract boolean updateVolumeName(String volumeuuid, String newName);
}
