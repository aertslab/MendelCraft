package org.aertslab.mendelcraft.blocks;

import java.io.Console;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.aertslab.mendelcraft.blockentities.BlockEntityRegistry;
import org.aertslab.mendelcraft.blockentities.LaboratoryBlockEntity;
import org.aertslab.mendelcraft.gui.LaboratoryContainer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.FireworkRocketItem.Shape;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class Laboratory extends HorizontalDirectionalBlock implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<LabPart> LABPART = EnumProperty.create("part", LabPart.class);

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[] { shape, Shapes.empty() };

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1],
                    Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        return buffer[0];
    }

    // protected static final VoxelShape LAB_NORTH_SHAPE = Block.box(0.0D, 0.0D,
    // 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape LAB_NORTH_SHAPE = Stream.of(
            Block.box(1, 0, 13, 3, 9, 15),
            Block.box(29, 0, 1, 31, 9, 3),
            Block.box(1, 0, 1, 3, 9, 3),
            Block.box(29, 0, 13, 31, 9, 15),
            Block.box(0, 9, 0, 32, 11, 16),
            Block.box(-14.25, 0, 1.75, -1.75, 18.5, 14.25),
            Block.box(-14.25, 18.5, 1.75, -1.75, 23.5, 8.25),
            Block.box(-13.25, 13.75, 14.25, -2.75, 18.5, 15.25),
            Block.box(-1.75, 1, 2.75, -0.75, 12.75, 13.25),
            Block.box(-1.75, 13.75, 2.75, -0.75, 18.5, 13.25),
            Block.box(-15.25, 1, 2.75, -14.25, 12.75, 13.25),
            Block.box(-15.25, 13.75, 2.75, -14.25, 18.5, 13.25),
            Block.box(-13.25, 1, 14.25, -2.75, 12.75, 15.25),
            Block.box(-13.25, 1, 0.75, -2.75, 22.75, 1.75)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    protected static final VoxelShape LAB_EAST_SHAPE = rotateShape(Direction.NORTH, Direction.EAST, LAB_NORTH_SHAPE);
    protected static final VoxelShape LAB_SOUTH_SHAPE = rotateShape(Direction.NORTH, Direction.SOUTH, LAB_NORTH_SHAPE);
    protected static final VoxelShape LAB_WEST_SHAPE = rotateShape(Direction.NORTH, Direction.WEST, LAB_NORTH_SHAPE);

    public Laboratory() {
        super(Properties.of(Material.METAL).noOcclusion().lightLevel((state) -> 2));
        this.registerDefaultState(this.stateDefinition.any().setValue(LABPART, LabPart.LEFT));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
            BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            pPos = getMainTile(pState.getValue(LABPART), pPos, pState);
            pState = pLevel.getBlockState(pPos);

            BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
            BlockPos finalPPos = pPos;
            System.out.println(finalPPos);
            System.out.println(tileEntity);
            if (tileEntity instanceof LaboratoryBlockEntity) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return new TranslatableComponent("screen.mendelcraft.laboratory");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                        return new LaboratoryContainer(i, pLevel, finalPPos, playerInventory);
                    }
                };
                NetworkHooks.openGui((ServerPlayer) pPlayer, containerProvider, tileEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.LABORATORY.get().create(pPos, pState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pState) {
        pState.add(FACING, LABPART);
    }

    private static BlockPos getMainTile(LabPart labPart, BlockPos pPos, BlockState pState) {
        switch (labPart) {
            case RIGHT:
                return pPos.relative(pState.getValue(FACING).getCounterClockWise());
            case LEFT:
                return pPos;
            case NS_LOWER:
                return pPos.relative(pState.getValue(FACING).getClockWise());
            case NS_UPPER:
                return pPos.relative(pState.getValue(FACING).getClockWise()).below();
            default:
                return pPos;
        }
    }

    private static BlockPos getFromMain(LabPart labPart, BlockPos pPos, Direction pFacing) {
        switch (labPart) {
            case RIGHT:
                return pPos.relative(pFacing.getClockWise());
            case LEFT:
                return pPos;
            case NS_LOWER:
                return pPos.relative(pFacing.getCounterClockWise());
            case NS_UPPER:
                return pPos.relative(pFacing.getCounterClockWise()).above();
            default:
                return pPos;
        }
    }

    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide) {
            LabPart labPart = pState.getValue(LABPART);
            BlockPos mainPos = getMainTile(labPart, pPos, pState);
            BlockState mainState = pLevel.getBlockState(mainPos);
            for (LabPart part : LabPart.values()) {
                BlockPos partPos = getFromMain(part, mainPos, mainState.getValue(FACING));
                BlockState partState = pLevel.getBlockState(partPos);
                if (partState.getBlock() == this && partState.getValue(LABPART) == part) {
                    pLevel.setBlock(partPos, Blocks.AIR.defaultBlockState(), 35);
                    pLevel.levelEvent(pPlayer, 2001, partPos, Block.getId(partState));
                }
            }
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.DESTROY;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        VoxelShape shape = LAB_EAST_SHAPE;
        switch (state.getValue(FACING)) {
            case NORTH:
                shape = LAB_NORTH_SHAPE;
                break;
            case SOUTH:
                shape = LAB_SOUTH_SHAPE;
                break;
            case WEST:
                shape = LAB_WEST_SHAPE;
                break;
            case EAST:
                shape = LAB_EAST_SHAPE;
                break;
            default:
                break;
        }
        // get xyz difference from maintile
        BlockPos mainPos = getMainTile(state.getValue(LABPART), pos, state);
        BlockPos diff = pos.subtract(mainPos);
        shape = shape.move(-diff.getX(), -diff.getY(), -diff.getZ());
        return shape;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        for (LabPart part : LabPart.values()) {
            BlockPos partPos = getFromMain(part, blockpos, context.getHorizontalDirection().getOpposite());
            if (!level.getBlockState(partPos).canBeReplaced(context) || !level.getWorldBorder().isWithinBounds(blockpos)) {
                return null;
            }
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }


    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlayer,
            ItemStack pItem) {
        super.setPlacedBy(pLevel, pPos, pState, pPlayer, pItem);
        if (!pLevel.isClientSide) {
            BlockPos mainPos = pPos;
            BlockState mainState = pState;
            for (LabPart part : LabPart.values()) {
                BlockPos partPos = getFromMain(part, mainPos, mainState.getValue(FACING));
                pLevel.setBlock(partPos, pState.setValue(LABPART, part), 3);
                pLevel.blockUpdated(partPos, Blocks.AIR);
                pState.updateNeighbourShapes(pLevel, pPos, 3);
                pState.updateNeighbourShapes(pLevel, partPos, 3);
            }
        }

    }
}
