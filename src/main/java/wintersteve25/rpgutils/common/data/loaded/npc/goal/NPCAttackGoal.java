package wintersteve25.rpgutils.common.data.loaded.npc.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import wintersteve25.rpgutils.common.entities.NPCEntity;

import java.util.EnumSet;

public class NPCAttackGoal extends Goal {

    private static final int ATTACK_INTERVAL = 20;

    protected final NPCEntity npcEntity;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private long lastCanUseCheck;

    public NPCAttackGoal(NPCEntity npcEntity) {
        this.npcEntity = npcEntity;
        this.speedModifier = 1;
        this.followingTargetEvenIfNotSeen = true;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        long i = this.npcEntity.level.getGameTime();
        if (i - this.lastCanUseCheck < ATTACK_INTERVAL) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.npcEntity.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                this.path = this.npcEntity.getNavigation().createPath(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.getAttackReachSqr(livingentity) >= this.npcEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                }
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.npcEntity.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.npcEntity.getNavigation().isDone();
        } else if (!this.npcEntity.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        } else {
            return !(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity)livingentity).isCreative();
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.npcEntity.getNavigation().moveTo(this.path, this.speedModifier);
        this.npcEntity.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        LivingEntity livingentity = this.npcEntity.getTarget();
        if (!EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.npcEntity.setTarget(null);
        }

        this.npcEntity.setAggressive(false);
        this.npcEntity.getNavigation().stop();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        LivingEntity target = this.npcEntity.getTarget();
        this.npcEntity.getLookControl().setLookAt(target, 30.0F, 30.0F);
        double d0 = this.npcEntity.distanceToSqr(target.getX(), target.getY(), target.getZ());
        this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
        if ((this.followingTargetEvenIfNotSeen || this.npcEntity.getSensing().canSee(target)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D || target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D || this.npcEntity.getRandom().nextFloat() < 0.05F)) {
            this.pathedTargetX = target.getX();
            this.pathedTargetY = target.getY();
            this.pathedTargetZ = target.getZ();
            this.ticksUntilNextPathRecalculation = 4 + this.npcEntity.getRandom().nextInt(7);
            if (d0 > 1024.0D) {
                this.ticksUntilNextPathRecalculation += 10;
            } else if (d0 > 256.0D) {
                this.ticksUntilNextPathRecalculation += 5;
            }

            if (!this.npcEntity.getNavigation().moveTo(target, this.speedModifier)) {
                this.ticksUntilNextPathRecalculation += 15;
            }
        }

        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        this.checkAndPerformAttack(target, d0);
    }

    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.npcEntity.swing(Hand.MAIN_HAND);
            this.npcEntity.doHurtTarget(pEnemy);
        }

    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = ATTACK_INTERVAL;
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected int getAttackInterval() {
        return ATTACK_INTERVAL;
    }

    protected double getAttackReachSqr(LivingEntity pAttackTarget) {
        return this.npcEntity.getBbWidth() * 2.0F * this.npcEntity.getBbWidth() * 2.0F + pAttackTarget.getBbWidth();
    }
}
