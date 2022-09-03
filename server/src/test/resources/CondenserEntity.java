import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CondenserEntity extends BlockEntity {

    // Store the current value of the number
    private int time = 0;
    // private int time_limit = 0;
    private int level = 0;

    public CondenserEntity(BlockPos pos, BlockState state) {
        super(SkyutilsMod.CONDENSER_ENTITY, pos, state);

        /*
         * Biome biome=world.getBiome(this.getPos()); if ((biome == Biomes.BADLANDS||
         * biome == Biomes.BADLANDS_PLATEAU|| biome == Biomes.DESERT|| biome ==
         * Biomes.DESERT_HILLS|| biome == Biomes.DESERT_LAKES)) { time=time*2; }
         */
    }

    public int getLvl() {
        return level;
    }

    public int getTime() {
        return time;
    }

    public void setLevel(int l) {
        level = l;
    }

    public void incLevel() {
        if (level < 7)
            level++;
    }

    public void setTime(int t) {
        time = t;
    }

    public void incTime() {
        time++;
    }

    public void empty() {
        level = 0;
        time = 0;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        // Save the current value of the number to the tag
        tag.putInt("number", time);
        tag.putInt("level", level);

    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        //System.out.println("fromTag");
        time = tag.getInt("number");
        level = tag.getInt("level");
    }



    public static void tick(Level world, BlockPos pos, BlockState state, CondenserEntity blockEntity) {
        if (!world.isClientSide) {
            int time_limit = 2400;
            Biome biome = world.getBiome(pos).value();
            float temperature = biome. getBaseTemperature();
            boolean raining = world.isRaining();

            if (temperature >= 0.95f) {
                time_limit = time_limit * 2;
            }
            if (biome.getPrecipitation() == Biome.Precipitation.RAIN && raining) {
                time_limit = (int) (time_limit * 0.05);
            }

            int d = time_limit / 7;

            if (state.getBlock() instanceof CondenserBlock) {
                CondenserBlock block = (CondenserBlock) state.getBlock();
                if (blockEntity.getTime() > time_limit) {
                    blockEntity.setTime(0);
                    if (blockEntity.getTime() % d == 0 && blockEntity.getLvl() < 7) {
                        blockEntity.incLevel();
                        block.incLevel(world, pos, state);
                    }
                }
                blockEntity.incTime();
                blockEntity.setChanged();
            }
        }
    }
}