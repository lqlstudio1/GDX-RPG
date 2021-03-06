package com.rpsg.rpg.system.controller;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.rpsg.gdxQuery.$;
import com.rpsg.rpg.core.RPG;
import com.rpsg.rpg.core.Setting;
import com.rpsg.rpg.io.Files;
import com.rpsg.rpg.object.base.Achievement;
import com.rpsg.rpg.object.base.BaseTask;
import com.rpsg.rpg.object.base.Task;
import com.rpsg.rpg.object.base.Task.TriggerType;
import com.rpsg.rpg.object.base.TaskInfo;

/** 成就、任务控制器 **/
@SuppressWarnings("unchecked")
public class TaskController {

	ArrayList<Task> currentTask;
	ArrayList<Achievement> currentAchievement = new ArrayList<>();
	List<TaskInfo> history = new ArrayList<>();

	/** 读取成就目录 */
	public void init() {
		Object ach = Files.load(Achievement.fileName);
		Object info = Files.load(TaskInfo.fileName);

		if (null != ach) {
			currentAchievement = (ArrayList<Achievement>) ach;
		}else{
			/**没有成就文件时，新建成就文件并读取所有成就*/
			for(int id : list(Gdx.files.internal(Setting.SCRIPT_DATA_ACHIEVEMENT)))
				currentAchievement.add(Achievement.fromJSON(id));
			saveAchievement();
		}
		
		if (null != info) 
			history = (ArrayList<TaskInfo>) info;
		else
			saveHistory();
	}
	
	private List<Integer> list(FileHandle file) {
		List<Integer> list = new ArrayList<>();
		int count = 0;
		while(file.child(++count + ".grd").exists())
			list.add(count);
		return list;
	}
	
	private void saveHistory() {
		Files.save(history, TaskInfo.fileName);
	}

	public void initTask(){
		currentTask = RPG.global.currentTask;
	}

	/** 保存成就目录到文件 */
	public void saveAchievement() {
		Files.save(currentAchievement, Achievement.fileName);
	}
	
	/** 遍历任务和成就，判断完成情况 */
	public void logic(){
		List<BaseTask> list = new ArrayList<>();
		if(currentTask != null){
			for(Task task : currentTask)
				if(task.trigger == TriggerType.auto)
					list.add(task);
		}
		for(Achievement ach : currentAchievement)
			if(ach.trigger == TriggerType.auto)
				list.add(ach);
		
		$.removeIf(list, BaseTask::canEnd, this::end);
	}
	
	
	/** 增加任务 */
	public void add(Task task){
		currentTask.add(task);
	}
	
	/** 增加任务 */
	public void add(int id){
		add(Task.fromJSON(id));
	}
	
	private Task getTask(int id){
		return $.getIf(currentTask, t-> t.id == id);
	}
	
	private Achievement getAchievement(int id){
		return $.getIf(currentAchievement, t-> t.id == id);
	}
	
	/** 查询是否正在进行任务*/
	public boolean has(int id){
		return currentTask.size() != 0 && $.test(currentTask, t-> t.id == id);
	}
	
	/** 查询是否做过某个任务(不是成就)*/
	public boolean hasDone(int id){
		return $.test(history, t-> t.id == id && t.type.equals(Task.class));
	}
	
	
	/** 查询某个任务是否可以完成，前提是这个任务存在的情况下*/
	public boolean canBeDone(int id){
		return has(id) && getTask(id).canEnd();
	}
	
	/** 获得当前进行中的任务（副本） */
	public List<Task> task(){
		return (List<Task>) currentTask.clone();
	}
	
	/** 获得当前进行中的成就（副本）*/
	public List<Achievement> achievement(){
		return (List<Achievement>) currentAchievement.clone();
	}
	
	/**完成一个任务（非成就）**/
	public void endTask(int id){
		end(getTask(id));
	}
	
	/**完成一个成就（非任务）**/
	public void endAchievement(int id){
		end(getAchievement(id));
	}
	
	/**强制让一个任务状态变为已完成（但并不提交）*/
	public Task forceStop(int id){
		Task task = getTask(id);
		if(task == null) return task;
		task.end.forceStop();
		return task;
	}
	
	/** 完成一个任务或成就 */
	public void end(BaseTask task) {
		if(task == null) return;
		
		history.add(TaskInfo.create(task));
		boolean isTask = task instanceof Task;
		if (isTask){
			currentTask.remove(task);
		}else{
			currentAchievement.remove(task);
			saveAchievement();
		}
		
		task.gain();
		
		RPG.toast.add((isTask ? "完成任务" : "获得成就") + "\n\"" + task.name + "\"\n" + task.description2, Color.SKY, 22, true, task.getIcon());
	}
}
