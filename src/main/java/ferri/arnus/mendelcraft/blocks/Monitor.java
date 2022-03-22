package ferri.arnus.mendelcraft.blocks;

import ferri.arnus.mendelcraft.blockentities.BlockEntityRegistry;
import ferri.arnus.mendelcraft.blockentities.MonitorBlockEntity;
import ferri.arnus.mendelcraft.gui.MonitorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class Monitor extends BaseEntityBlock{

	public Monitor() {
		super(Properties.of(Material.GLASS));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
		return BlockEntityRegistry.MONITOR.get().create(p_153215_, p_153216_);
	}
	
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (!pLevel.isClientSide) {
			BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
            if (tileEntity instanceof MonitorBlockEntity) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return new TranslatableComponent("screen.mendelcraft.monitor");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                        return new MonitorMenu(i, pLevel, pPos, playerInventory);
                    }
                };
                NetworkHooks.openGui((ServerPlayer) pPlayer, containerProvider, tileEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Monitor named container provider is missing!");
            }
		}
		return InteractionResult.SUCCESS;
	}

}
