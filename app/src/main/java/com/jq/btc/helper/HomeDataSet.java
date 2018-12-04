package com.jq.btc.helper;

import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.BGlucoseEntity;
import com.jq.code.model.BPressEntity;
import com.jq.code.model.DataType;
import com.jq.code.model.ExerciseDietEntity;
import com.jq.code.model.PutBase;
import com.jq.code.model.WeightEntity;
import com.jq.code.model.sport.SubmitFoodEntity;
import com.jq.code.model.sport.SubmitSportEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2016/7/5.
 */
public class HomeDataSet {

    /**
     * 动态显示数据列表
     */
    private static Vector<PutBase> putBases = new Vector<PutBase>();
    /**
     * 动态显示数据时间列表
     */
    private static Vector<String> dateList = new Vector<String>();

    public static long getDataEnd() {
        if (putBases.isEmpty()) {
            return System.currentTimeMillis();
        }
//        sort();
        if (putBases.get(putBases.size() - 1) instanceof ExerciseDietEntity) {
            return TimeUtil.getTimestamp(putBases.get(putBases.size() - 1).getMeasure_time() + " 23:59:59");
        }
        return TimeUtil.getTimestamp(putBases.get(putBases.size() - 1).getMeasure_time());
    }

//    public synchronized static void notifyChanged() {
//        sort();
//        dataSetObserver.onChanged(putBases);
//    }

    private static void sort() {
        Collections.sort(putBases, new Comparator<PutBase>() {
            @Override
            public int compare(PutBase lhs, PutBase rhs) {
                return rhs.getMeasure_time().compareTo(lhs.getMeasure_time());
            }
        });
        Collections.sort(dateList, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return rhs.compareTo(lhs);
            }
        });
    }

    public static int getSize() {
        return putBases.size();
    }

    public static void clear() {
        putBases.clear();
        dateList.clear();
    }

    public static void reset(List<PutBase> bases) {
        putBases.clear();
        dateList.clear();
        if (bases == null) {
            bases = new ArrayList<>();
        }
        putBases.addAll(bases);
        addDate(bases);
    }


    public static void addFooter(List<PutBase> bases) {
        putBases.addAll(bases);
        addDate(bases);
    }

    public static void addHead(PutBase putBase) {
        if (putBases.isEmpty()) {
            putBases.add(putBase);
            dateList.add(putBase.getMeasure_time());
        } else {
            putBases.add(0, putBase);
            dateList.add(0, putBase.getMeasure_time());
        }
        setAddType(putBase);
    }

    private static void setAddType(PutBase putBase) {
        if (putBase instanceof WeightEntity) {
            DataType.curAddType = DataType.WEIGHT;
        } else if (putBase instanceof BPressEntity) {
            DataType.curAddType = DataType.BP;
        } else if (putBase instanceof BGlucoseEntity) {
            DataType.curAddType = DataType.BSL;
        } else if (putBase instanceof ExerciseDietEntity) {
            DataType.curAddType = DataType.FOOD;
        }
    }

    private static void addDate(List<PutBase> bases) {
        for (PutBase base : bases) {
            dateList.add(base.getMeasure_time());
        }
    }

    public static void putBasesfilter(List<PutBase> bases) {
        if (bases == null) return;
        if (putBases.isEmpty()) {
            addFooter(bases);
        } else {
            List<PutBase> tmPutBases = new ArrayList<>();
            PutBase tmpBase = putBases.get(putBases.size() - 1);
            for (PutBase base : bases) {
                if (base.getMeasure_time().compareTo(tmpBase.getMeasure_time()) > 0) {
                    tmPutBases.add(base);
                }
            }
            if (!tmPutBases.isEmpty())
                addFooter(tmPutBases);
        }
    }

    public static void addPutBase(PutBase putBase) {
        boolean isAdded = false;
        for (int i = 0; i < putBases.size(); i++) {
            if (TimeUtil.getTimestamp(putBases.get(i).getMeasure_time()) <= TimeUtil.getTimestamp(putBase.getMeasure_time())) {
                putBases.add(i, putBase);
                dateList.add(i, putBase.getMeasure_time());
                isAdded = true;
                break;
            }
        }
        if (!isAdded && putBases.size() < 10) {
            if (putBases.isEmpty()) {
                putBases.add(putBase);
                dateList.add(putBase.getMeasure_time());
            } else {
                putBases.add(putBases.size(), putBase);
                dateList.add(dateList.size(), putBase.getMeasure_time());
            }
            return;
        }
        if (isAdded) {
            setAddType(putBase);
        } else {
            dataSetObserver.rangeOver(putBase);
        }
    }

    public static void remove(PutBase base) {
        if (dateList.contains(base.getMeasure_time())) {
            int index = dateList.indexOf(base.getMeasure_time());
            putBases.remove(index);
            dateList.remove(index);
        }
    }

    public static void removeSport(SubmitSportEntity entity) {
        String time = entity.getDate() + " 23:59:59";
        boolean hasData = dateList.contains(time);
        if (hasData) {
            int index = dateList.indexOf(time);
            PutBase base = putBases.get(index);
            if (base instanceof ExerciseDietEntity) {
                List<SubmitSportEntity> sports = ((ExerciseDietEntity) base).getSports();
                if (sports != null && !sports.isEmpty()) {
                    for (int i = 0; i < sports.size(); i++) {
                        if (sports.get(i).get_id() == entity.get_id()) {
                            sports.remove(i);
                            ((ExerciseDietEntity) base).setSports(sports);
                            removSportOrFood(index);
                            break;
                        }
                    }
                }
            }
        }
    }

    private static void removSportOrFood(int index) {
        ExerciseDietEntity dietEntity = (ExerciseDietEntity) putBases.get(index);
        boolean isFoodEmpty = dietEntity.getFoods() == null || dietEntity.getFoods().isEmpty();
        boolean isSportEmpty = dietEntity.getSports() == null || dietEntity.getSports().isEmpty();
        if (isFoodEmpty && isSportEmpty) {
            putBases.remove(index);
            dateList.remove(index);
        }
    }

    public static void removeFood(SubmitFoodEntity entity) {
        String time = entity.getDate() + " 23:59:59";
        boolean hasData = dateList.contains(time);
        if (hasData) {
            int index = dateList.indexOf(time);
            PutBase base = putBases.get(index);
            if (base instanceof ExerciseDietEntity) {
                List<SubmitFoodEntity> foods = ((ExerciseDietEntity) base).getFoods();
                if (foods != null && !foods.isEmpty()) {
                    for (int i = 0; i < foods.size(); i++) {
                        if (foods.get(i).get_id() == entity.get_id()) {
                            foods.remove(i);
                            ((ExerciseDietEntity) base).setFoods(foods);
                            removSportOrFood(index);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void putBaseofSport(List<SubmitSportEntity> infos) {
        String time = infos.get(0).getDate() + " 23:59:59";
        boolean hasData = dateList.contains(time);
        if (hasData) {
            int index = dateList.indexOf(time);
            PutBase base = putBases.get(index);
            if (base instanceof ExerciseDietEntity) {
                List<SubmitSportEntity> entities = new ArrayList<>();
                entities.addAll(infos);
                if (((ExerciseDietEntity) base).getSports() != null) {
                    entities.addAll(((ExerciseDietEntity) base).getSports());
                }
                ((ExerciseDietEntity) base).setSports(entities);
            } else {
                ExerciseDietEntity dietEntity = new ExerciseDietEntity();
                dietEntity.setSports(infos);
                dietEntity.setMeasure_time(time);
                putBases.add(index, dietEntity);
                dateList.add(index, time);
            }
        } else {
            ExerciseDietEntity dietEntity = new ExerciseDietEntity();
            dietEntity.setSports(infos);
            dietEntity.setMeasure_time(time);
            addPutBase(dietEntity);
        }
        DataType.curAddType = DataType.FOOD;
    }

    public static void putBaseofFood(List<SubmitFoodEntity> infos) {
        String time = infos.get(0).getDate() + " 23:59:59";
        boolean hasData = dateList.contains(time);
        if (hasData) {
            int index = dateList.indexOf(time);
            PutBase base = putBases.get(index);
            if (base instanceof ExerciseDietEntity) {
                List<SubmitFoodEntity> entities = new ArrayList<>();
                entities.addAll(infos);
                if (((ExerciseDietEntity) base).getFoods() != null) {
                    entities.addAll(((ExerciseDietEntity) base).getFoods());
                }
                ((ExerciseDietEntity) base).setFoods(entities);
            } else {
                ExerciseDietEntity dietEntity = new ExerciseDietEntity();
                dietEntity.setFoods(infos);
                dietEntity.setMeasure_time(time);
                putBases.add(index, dietEntity);
                dateList.add(index, time);
            }
        } else {
            ExerciseDietEntity dietEntity = new ExerciseDietEntity();
            dietEntity.setFoods(infos);
            dietEntity.setMeasure_time(time);
            addPutBase(dietEntity);
        }
        DataType.curAddType = DataType.FOOD;
    }

    private static HomeDataSetObserver dataSetObserver;

    public static void register(HomeDataSetObserver setObserver) {
        dataSetObserver = setObserver;
    }
}
