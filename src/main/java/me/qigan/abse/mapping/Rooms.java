package me.qigan.abse.mapping;

import me.qigan.abse.config.AddressedData;
import me.qigan.abse.mapping.rooms.r1x1.*;
import me.qigan.abse.mapping.rooms.r1x2.*;
import me.qigan.abse.mapping.rooms.r1x3.RoomGravel;
import me.qigan.abse.mapping.rooms.r1x3.RoomRedBlue;
import me.qigan.abse.mapping.rooms.r1x4.RoomMossy;
import me.qigan.abse.mapping.rooms.r1x4.RoomQuartzKnight;
import me.qigan.abse.mapping.rooms.r1x4.RoomWaterfall;
import me.qigan.abse.mapping.rooms.r2x2.*;
import me.qigan.abse.mapping.rooms.rL.RoomLavaRevine;
import me.qigan.abse.mapping.routing.BBox;
import me.qigan.abse.mapping.routing.Route;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * !!!IMPORTANT!!!
 * For future me: update max id when routing
 *
 *
 */
public class Rooms {


    public static List<RoomTemplate> rooms = new ArrayList<>();
    public static Map<Integer, Route> routes = new HashMap<>();



    /**
     *
     *                                         ROOMS
     *
     *                                   Current max id is 27
     *
     *
     *                                          ROUTING
     *
     * Coloring format:
     *              RED - click on it
     *              GREEN - secret
     *              CYAN - etherwarp in it
     *              YELLOW - stonk here
     *              PURPLE - pearl here
     *
     */
    public static void setup() {
        registerRoom(new RoomScaffolding());
        registerRoom(new RoomRacoon());
        registerRoom(new RoomDueces());
        registerRoom(new RoomBridges());
        registerRoom(new RoomStairs());
        registerRoom(new RoomMines());
        registerRoom(new RoomCathedral());
        registerRoom(new RoomWaterfall());
        registerRoom(new RoomFlags());
        registerRoom(new RoomGravel());
        registerRoom(new RoomMuseum());
        registerRoom(new RoomChains());
        registerRoom(new RoomGrassRuins());
        registerRoom(new RoomMossy());
        registerRoom(new RoomDoubleDiamond());
        registerRoom(new RoomPedestal());
        registerRoom(new RoomLongHall());
        registerRoom(new RoomRails());
        registerRoom(new RoomTemple());
        registerRoom(new RoomRedBlue());
        registerRoom(new RoomBalcony());
        registerRoom(new RoomQuartzKnight());
        registerRoom(new RoomCrypt());
        registerRoom(new RoomLavaRevine());
        registerRoom(new RoomAtlas());
        registerRoom(new RoomPressurePlates());
    }

    public static void registerRoom(RoomTemplate temple) {
        rooms.add(temple);
        routes.put(temple.getId(), temple.route());
    }

    public static int match(Room rm) {
        for (RoomTemplate temple : rooms) {
            if (check(temple.hooks(), rm)) return temple.getId();
        }
        return -1;
    }

    private static boolean check(Collection<AddressedData<BlockPos, Block>> rms, Room rm) {
        for (AddressedData<BlockPos, Block> ele : rms) {
            BlockPos pos = rm.transformInnerCoordinate(ele.getNamespace());
            //MappingController.debug.add(pos);
            if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() != ele.getObject()) return false;
        }
        return true;
    }

}
