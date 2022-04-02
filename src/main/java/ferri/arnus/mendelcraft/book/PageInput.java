package ferri.arnus.mendelcraft.book;

import java.util.function.UnaryOperator;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TextComponent;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

public class PageInput implements ICustomComponent{
	private int x;
	private int y;

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> arg0) {
		
	}

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
	}

	@Override
	public void render(PoseStack arg0, IComponentRenderContext arg1, float arg2, int arg3, int arg4) {
		
	}
	
	@Override
	public void onDisplayed(IComponentRenderContext context) {
		EditBox editBox = new EditBox(Minecraft.getInstance().font, this.x, this.y, 120, 20, new TextComponent("test"));
		editBox.setMessage(new TextComponent("maybe?"));
		editBox.insertText("this?");
		((GuiBookEntry)context).addRenderableWidget(editBox);
	}
}
