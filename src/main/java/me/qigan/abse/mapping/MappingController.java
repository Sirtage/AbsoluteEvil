package me.qigan.abse.mapping;

import me.qigan.abse.Index;
import me.qigan.abse.mapping.mod.Remapping;
import me.qigan.abse.sync.Sync;
import me.qigan.abse.vp.Esp;
import me.qigan.abse.vp.S2Dtype;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingController {

    /**
     * RIGHT READING SEQUENCE
     *                 String str = "\n";
     *                 for (int i w= 0; i < 6; i++) {
     *                     for (int j = 0; j < 6; j++) {
     *                         str += map[j][i] + " ";
     *                     }
     *                     str += "\n";
     *                 }
     *                 System.out.println(str);   this.transformInnerCoordinate(pos.add(i, 0, 0))
     */


    public static List<BlockPos> debug = new ArrayList<>();

    public int[][] map = null;
    public Map<Integer, Room> roomMap = new HashMap<>();
    public int[] playerCell;

    public void update() {
        if (map != null) map = Mapping.sync(map);
    }

    public static int[] calcPlayerCell() {
        return Mapping.realToCell(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posZ);
    }

    public int getCurrentCellIter() {
        int[] coord = calcPlayerCell();
        if (coord[0] < 0 || coord[0] >= 6 || coord[1] < 0 || coord[1] >= 6) return -1;
        return map[coord[0]][coord[1]];
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    void onLoad(WorldEvent.Load e) {
        map = null;
        playerCell = new int[]{-1, -1};
        roomMap.clear();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                map = Mapping.scanFull(calcPlayerCell());
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }).start();
    }

    @SubscribeEvent
    void tick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.END || !Sync.inDungeon || Minecraft.getMinecraft().thePlayer == null) return;
        int[] newPos = calcPlayerCell();
        if (newPos[0] != playerCell[0] || newPos[1] != playerCell[1]) {
            playerCell = newPos;
            update();

            //TODO: FIX THIS VERY TEMPORARY SOLUTIONd
            if (map == null) return;
            if (playerCell[0] >= 0 && playerCell[0] < 6 && playerCell[1] >= 0 && playerCell[1] < 6) {
                int iter = map[playerCell[0]][playerCell[1]];
                if (!roomMap.containsKey(iter)) roomMap.put(iter, new Room(iter).define(map));

                if (Index.MAIN_CFG.getBoolVal("remap")) {
                    Room rm = roomMap.get(iter);
                    if (Rooms.routes.containsKey(rm.getId())) {
                        Rooms.routes.get(rm.getId()).placeRoute(rm);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    void ovr(RenderGameOverlayEvent.Text e) {
        if (!Index.MAIN_CFG.getBoolVal("remap_debug") || map == null) return;
        Point pt = new Point(100, 300);
        int[] k = Mapping.realToCell(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posZ);
        Esp.drawOverlayString(k[0] + ":" + k[1], pt.x, pt.y-30, Color.cyan, S2Dtype.DEFAULT);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Esp.drawCenteredString((k[0] == i && k[1] == j ? "\u00A7a" : "\u00A7c") + map[i][j], pt.x+15*i, pt.y+15*j, 0xFFFFFF, S2Dtype.DEFAULT);
            }
        }
        Esp.drawOverlayString(Remapping.createRoomInfo(), pt.x, pt.y+85, Color.cyan, S2Dtype.DEFAULT);
    }

    @SubscribeEvent
    void rend(RenderWorldLastEvent e) {
        if (!Index.MAIN_CFG.getBoolVal("remap_debug") || map == null || Minecraft.getMinecraft().theWorld == null) return;
        for (BlockPos pos : debug) {
            Esp.autoBox3D(pos, Color.red, 2f, true);
        }
    }
}
