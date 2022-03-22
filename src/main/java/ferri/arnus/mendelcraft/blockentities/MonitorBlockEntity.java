package ferri.arnus.mendelcraft.blockentities;

import java.util.ArrayList;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MonitorBlockEntity extends BlockEntity{
	private ArrayList<String> questions = new ArrayList<>(5);
	private int answer = 1;

	public MonitorBlockEntity( BlockPos p_155229_, BlockState p_155230_) {
		super(BlockEntityRegistry.MONITOR.get(), p_155229_, p_155230_);
	}
	
	public ArrayList<String> getQuestions() {
		return questions;
	}
	
	public void setQuestions(int index, String question) {
		if (index > this.questions.size()) {
			return;
		}
		this.questions.set(index, question);
	}
	
	public void setAnswer(int answer) {
		this.answer = answer;
	}
	
	public int getAnswer() {
		return answer;
	}

}
