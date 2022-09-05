package wintersteve25.rpgutils.common.data.loaded.quest.objectives.predicates;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.FishingPredicate;
import net.minecraft.scoreboard.Team;
import net.minecraft.tags.ITag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import wintersteve25.rpgutils.common.data.saveddata.NpcIDMapping;

import javax.annotation.Nullable;

public class EntityPredicate {
    public static final EntityPredicate ANY = new EntityPredicate(EntityTypePredicate.ANY, DistancePredicate.ANY, LocationPredicate.ANY, MobEffectsPredicate.ANY, NBTPredicate.ANY, EntityFlagsPredicate.ANY, EntityEquipmentPredicate.ANY, PlayerPredicate.ANY, FishingPredicate.ANY, null, null, null);
    
    private final EntityTypePredicate entityType;
    private final DistancePredicate distanceToPlayer;
    private final LocationPredicate location;
    private final MobEffectsPredicate effects;
    private final NBTPredicate nbt;
    private final EntityFlagsPredicate flags;
    private final EntityEquipmentPredicate equipment;
    private final PlayerPredicate player;
    private final FishingPredicate fishingHook;
    private final EntityPredicate vehicle;
    private final EntityPredicate targetedEntity;
    @Nullable
    private final String team;
    @Nullable
    private final ResourceLocation catType;
    @Nullable
    private final String npcID;

    private EntityPredicate(EntityTypePredicate entityType, DistancePredicate distanceToPlayer, LocationPredicate location, MobEffectsPredicate effects, NBTPredicate nbt, EntityFlagsPredicate flags, EntityEquipmentPredicate equipment, PlayerPredicate player, FishingPredicate fishingHook, @Nullable String team, @Nullable ResourceLocation catType, @Nullable String npcID) {
        this.entityType = entityType;
        this.distanceToPlayer = distanceToPlayer;
        this.location = location;
        this.effects = effects;
        this.nbt = nbt;
        this.flags = flags;
        this.equipment = equipment;
        this.player = player;
        this.fishingHook = fishingHook;
        this.npcID = npcID;
        this.vehicle = this;
        this.targetedEntity = this;
        this.team = team;
        this.catType = catType;
    }

    private EntityPredicate(EntityTypePredicate entityType, DistancePredicate distanceToPlayer, LocationPredicate location, MobEffectsPredicate effects, NBTPredicate nbt, EntityFlagsPredicate flags, EntityEquipmentPredicate equipment, PlayerPredicate player, FishingPredicate fishingHook, EntityPredicate vehicle, EntityPredicate targetedEntity, @Nullable String team, @Nullable ResourceLocation catType, @Nullable String npcID) {
        this.entityType = entityType;
        this.distanceToPlayer = distanceToPlayer;
        this.location = location;
        this.effects = effects;
        this.nbt = nbt;
        this.flags = flags;
        this.equipment = equipment;
        this.player = player;
        this.fishingHook = fishingHook;
        this.vehicle = vehicle;
        this.targetedEntity = targetedEntity;
        this.team = team;
        this.catType = catType;
        this.npcID = npcID;
    }

    public boolean matches(ServerPlayerEntity pPlayer, @Nullable Entity pEntity) {
        return this.matches(pPlayer.getLevel(), pPlayer.position(), pEntity);
    }

    public boolean matches(ServerWorld pLevel, @Nullable Vector3d pVector, @Nullable Entity pEntity) {
        if (this == ANY) {
            return true;
        } else if (pEntity == null) {
            return false;
        } else if (!this.entityType.matches(pEntity.getType())) {
            return false;
        } else {
            if (pVector == null) {
                if (this.distanceToPlayer != DistancePredicate.ANY) {
                    return false;
                }
            } else if (!this.distanceToPlayer.matches(pVector.x, pVector.y, pVector.z, pEntity.getX(), pEntity.getY(), pEntity.getZ())) {
                return false;
            }

            if (!this.location.matches(pLevel, pEntity.getX(), pEntity.getY(), pEntity.getZ())) {
                return false;
            } else if (!this.effects.matches(pEntity)) {
                return false;
            } else if (!this.nbt.matches(pEntity)) {
                return false;
            } else if (!this.flags.matches(pEntity)) {
                return false;
            } else if (!this.equipment.matches(pEntity)) {
                return false;
            } else if (!this.player.matches(pEntity)) {
                return false;
            } else if (!this.fishingHook.matches(pEntity)) {
                return false;
            } else if (!this.vehicle.matches(pLevel, pVector, pEntity.getVehicle())) {
                return false;
            } else if (!this.targetedEntity.matches(pLevel, pVector, pEntity instanceof MobEntity ? ((MobEntity)pEntity).getTarget() : null)) {
                return false;
            } else {
                if (this.npcID != null) {
                    NpcIDMapping mapping = NpcIDMapping.get(pLevel);
                    if (mapping == null || !mapping.has(npcID)) {
                        return false;
                    }
                    
                    if (!mapping.get(npcID).equals(pEntity.getUUID())) {
                        return false;
                    }
                }
                
                if (this.team != null) {
                    Team team = pEntity.getTeam();
                    if (team == null || !this.team.equals(team.getName())) {
                        return false;
                    }
                }

                return this.catType == null || pEntity instanceof CatEntity && ((CatEntity)pEntity).getResourceLocation().equals(this.catType);
            }
        }
    }

    public static EntityPredicate fromJson(@Nullable JsonElement pElement) {
        if (pElement != null && !pElement.isJsonNull()) {
            JsonObject jsonobject = JSONUtils.convertToJsonObject(pElement, "entity");
            EntityTypePredicate entitytypepredicate = EntityTypePredicate.fromJson(jsonobject.get("type"));
            DistancePredicate distancepredicate = DistancePredicate.fromJson(jsonobject.get("distance"));
            LocationPredicate locationpredicate = LocationPredicate.fromJson(jsonobject.get("location"));
            MobEffectsPredicate mobeffectspredicate = MobEffectsPredicate.fromJson(jsonobject.get("effects"));
            NBTPredicate nbtpredicate = NBTPredicate.fromJson(jsonobject.get("nbt"));
            EntityFlagsPredicate entityflagspredicate = EntityFlagsPredicate.fromJson(jsonobject.get("flags"));
            EntityEquipmentPredicate entityequipmentpredicate = EntityEquipmentPredicate.fromJson(jsonobject.get("equipment"));
            PlayerPredicate playerpredicate = PlayerPredicate.fromJson(jsonobject.get("player"));
            FishingPredicate fishingpredicate = FishingPredicate.fromJson(jsonobject.get("fishing_hook"));
            EntityPredicate entitypredicate = fromJson(jsonobject.get("vehicle"));
            EntityPredicate entitypredicate1 = fromJson(jsonobject.get("targeted_entity"));
            String s = JSONUtils.getAsString(jsonobject, "team", (String)null);
            ResourceLocation resourcelocation = jsonobject.has("catType") ? new ResourceLocation(JSONUtils.getAsString(jsonobject, "catType")) : null;
            String npcID = JSONUtils.getAsString(jsonobject, "npcID", (String)null);
            return (new Builder()).entityType(entitytypepredicate).distance(distancepredicate).located(locationpredicate).effects(mobeffectspredicate).nbt(nbtpredicate).flags(entityflagspredicate).equipment(entityequipmentpredicate).player(playerpredicate).fishingHook(fishingpredicate).team(s).vehicle(entitypredicate).targetedEntity(entitypredicate1).catType(resourcelocation).npcID(npcID).build();
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject jsonobject = new JsonObject();
            jsonobject.add("type", this.entityType.serializeToJson());
            jsonobject.add("distance", this.distanceToPlayer.serializeToJson());
            jsonobject.add("location", this.location.serializeToJson());
            jsonobject.add("effects", this.effects.serializeToJson());
            jsonobject.add("nbt", this.nbt.serializeToJson());
            jsonobject.add("flags", this.flags.serializeToJson());
            jsonobject.add("equipment", this.equipment.serializeToJson());
            jsonobject.add("player", this.player.serializeToJson());
            jsonobject.add("fishing_hook", this.fishingHook.serializeToJson());
            jsonobject.add("vehicle", this.vehicle.serializeToJson());
            jsonobject.add("targeted_entity", this.targetedEntity.serializeToJson());
            if (this.team != null) {
                jsonobject.addProperty("team", this.team);
            }
            if (this.catType != null) {
                jsonobject.addProperty("catType", this.catType.toString());
            }
            if (this.npcID != null) {
                jsonobject.addProperty("npcID", npcID);
            }

            return jsonobject;
        }
    }

    public static class Builder {
        private EntityTypePredicate entityType = EntityTypePredicate.ANY;
        private DistancePredicate distanceToPlayer = DistancePredicate.ANY;
        private LocationPredicate location = LocationPredicate.ANY;
        private MobEffectsPredicate effects = MobEffectsPredicate.ANY;
        private NBTPredicate nbt = NBTPredicate.ANY;
        private EntityFlagsPredicate flags = EntityFlagsPredicate.ANY;
        private EntityEquipmentPredicate equipment = EntityEquipmentPredicate.ANY;
        private PlayerPredicate player = PlayerPredicate.ANY;
        private FishingPredicate fishingHook = FishingPredicate.ANY;
        private EntityPredicate vehicle = EntityPredicate.ANY;
        private EntityPredicate targetedEntity = EntityPredicate.ANY;
        private String team;
        private ResourceLocation catType;
        private String npcID;

        public static EntityPredicate.Builder entity() {
            return new EntityPredicate.Builder();
        }

        public EntityPredicate.Builder of(EntityType<?> pType) {
            this.entityType = EntityTypePredicate.of(pType);
            return this;
        }

        public EntityPredicate.Builder of(ITag<EntityType<?>> pType) {
            this.entityType = EntityTypePredicate.of(pType);
            return this;
        }

        public EntityPredicate.Builder of(ResourceLocation pCatType) {
            this.catType = pCatType;
            return this;
        }

        public EntityPredicate.Builder entityType(EntityTypePredicate pType) {
            this.entityType = pType;
            return this;
        }

        public EntityPredicate.Builder distance(DistancePredicate pDistance) {
            this.distanceToPlayer = pDistance;
            return this;
        }

        public EntityPredicate.Builder located(LocationPredicate pLocation) {
            this.location = pLocation;
            return this;
        }

        public EntityPredicate.Builder effects(MobEffectsPredicate pEffects) {
            this.effects = pEffects;
            return this;
        }

        public EntityPredicate.Builder nbt(NBTPredicate pNbt) {
            this.nbt = pNbt;
            return this;
        }

        public EntityPredicate.Builder flags(EntityFlagsPredicate pFlags) {
            this.flags = pFlags;
            return this;
        }

        public EntityPredicate.Builder equipment(EntityEquipmentPredicate pEquipment) {
            this.equipment = pEquipment;
            return this;
        }

        public EntityPredicate.Builder player(PlayerPredicate pPlayer) {
            this.player = pPlayer;
            return this;
        }

        public EntityPredicate.Builder fishingHook(FishingPredicate pFishing) {
            this.fishingHook = pFishing;
            return this;
        }

        public EntityPredicate.Builder vehicle(EntityPredicate pMount) {
            this.vehicle = pMount;
            return this;
        }

        public EntityPredicate.Builder targetedEntity(EntityPredicate pTarget) {
            this.targetedEntity = pTarget;
            return this;
        }

        public EntityPredicate.Builder team(@Nullable String pTeam) {
            this.team = pTeam;
            return this;
        }

        public EntityPredicate.Builder catType(@Nullable ResourceLocation pCatType) {
            this.catType = pCatType;
            return this;
        }

        public EntityPredicate.Builder npcID(@Nullable String npcID) {
            this.npcID = npcID;
            return this;
        }

        public EntityPredicate build() {
            return new EntityPredicate(this.entityType, this.distanceToPlayer, this.location, this.effects, this.nbt, this.flags, this.equipment, this.player, this.fishingHook, this.vehicle, this.targetedEntity, this.team, this.catType, this.npcID);
        }
    }
}
