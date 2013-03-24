package net.minecraft.server;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class DedicatedServer extends MinecraftServer implements IServer {

    private final List k = Collections.synchronizedList(new ArrayList());
    private final ILogAgent l;
    private RConThreadQuery m;
    private RConThreadMain n;
    private PropertyManager o;
    private boolean p;
    private EnumGameType q;
    private NetworkListenThread r;
    private boolean s = false;

    public DedicatedServer(File file1) {
        super(file1);
        this.l = new LogAgent("Minecraft-Server", (String) null, (new File(file1, "server.log")).getAbsolutePath());
        new DedicatedServerSleepThread(this);
    }

    protected boolean c() {
        DedicatedServerCommandThread dedicatedservercommandthread = new DedicatedServerCommandThread(this);

        dedicatedservercommandthread.setDaemon(true);
        dedicatedservercommandthread.start();
        this.al().a("Starting minecraft server version 1.5.1");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            this.al().b("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        this.al().a("Loading properties");
        this.o = new PropertyManager(new File("server.properties"), this.al());
        if (this.I()) {
            this.d("127.0.0.1");
        }
        else {
            this.d(this.o.a("online-mode", true));
            this.d(this.o.a("server-ip", ""));
        }

        this.e(this.o.a("spawn-animals", true));
        this.f(this.o.a("spawn-npcs", true));
        this.g(this.o.a("pvp", true));
        this.h(this.o.a("allow-flight", false));
        this.n(this.o.a("texture-pack", ""));
        this.o(this.o.a("motd", "A Minecraft Server"));
        if (this.o.a("difficulty", 1) < 0) {
            this.o.a("difficulty", Integer.valueOf(0));
        }
        else if (this.o.a("difficulty", 1) > 3) {
            this.o.a("difficulty", Integer.valueOf(3));
        }

        this.p = this.o.a("generate-structures", true);
        int i0 = this.o.a("gamemode", EnumGameType.b.a());

        this.q = WorldSettings.a(i0);
        this.al().a("Default game type: " + this.q);
        InetAddress inetaddress = null;

        if (this.l().length() > 0) {
            try {
                inetaddress = InetAddress.getByName(this.l());
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (this.G() < 0) {
            this.b(this.o.a("server-port", 25565));
        }

        this.al().a("Generating keypair");
        this.a(CryptManager.b());
        this.al().a("Starting Minecraft server on " + (this.l().length() == 0 ? "*" : this.l()) + ":" + this.G());

        try {
            this.r = new DedicatedServerListenThread(this, inetaddress, this.G());
        } catch (IOException ioexception) {
            this.al().b("**** FAILED TO BIND TO PORT!");
            this.al().b("The exception was: {0}", new Object[] { ioexception.toString() });
            this.al().b("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.U()) {
            this.al().b("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            this.al().b("The server will make no attempt to authenticate usernames. Beware.");
            this.al().b("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            this.al().b("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
        }

        this.a((ServerConfigurationManager) (new DedicatedPlayerList(this)));
        long i1 = System.nanoTime();

        if (this.J() == null) {
            this.l(this.o.a("level-name", "world"));
        }

        String s0 = this.o.a("level-seed", "");
        String s1 = this.o.a("level-type", "DEFAULT");
        String s2 = this.o.a("generator-settings", "");
        long i2 = (new Random()).nextLong();

        if (s0.length() > 0) {
            try {
                long i3 = Long.parseLong(s0);

                if (i3 != 0L) {
                    i2 = i3;
                }
            } catch (NumberFormatException numberformatexception) {
                i2 = (long) s0.hashCode();
            }
        }

        WorldType worldtype = WorldType.a(s1);

        if (worldtype == null) {
            worldtype = WorldType.b;
        }

        this.d(this.o.a("max-build-height", 256));
        this.d((this.ab() + 8) / 16 * 16);
        this.d(MathHelper.a(this.ab(), 64, 256));
        this.o.a("max-build-height", Integer.valueOf(this.ab()));
        this.al().a("Preparing level \"" + this.J() + "\"");
        this.a(this.J(), this.J(), i2, worldtype, s2);
        long i4 = System.nanoTime() - i1;
        String s3 = String.format("%.3fs", new Object[] { Double.valueOf((double) i4 / 1.0E9D) });

        this.al().a("Done (" + s3 + ")! For help, type \"help\" or \"?\"");
        if (this.o.a("enable-query", false)) {
            this.al().a("Starting GS4 status listener");
            this.m = new RConThreadQuery(this);
            this.m.a();
        }

        if (this.o.a("enable-rcon", false)) {
            this.al().a("Starting remote control listener");
            this.n = new RConThreadMain(this);
            this.n.a();
        }

        return true;
    }

    public boolean f() {
        return this.p;
    }

    public EnumGameType g() {
        return this.q;
    }

    public int h() {
        return this.o.a("difficulty", 1);
    }

    public boolean i() {
        return this.o.a("hardcore", false);
    }

    protected void a(CrashReport crashreport) {
        while (this.m()) {
            this.am();

            try {
                Thread.sleep(10L);
            } catch (InterruptedException interruptedexception) {
                interruptedexception.printStackTrace();
            }
        }
    }

    public CrashReport b(CrashReport crashreport) {
        crashreport = super.b(crashreport);
        crashreport.g().a("Is Modded", (Callable) (new CallableType(this)));
        crashreport.g().a("Type", (Callable) (new CallableServerType(this)));
        return crashreport;
    }

    protected void p() {
        System.exit(0);
    }

    public void r() { // CanaryMod: protected => public
        super.r();
        this.am();
    }

    public boolean s() {
        return this.o.a("allow-nether", true);
    }

    public boolean L() {
        return this.o.a("spawn-monsters", true);
    }

    public void a(PlayerUsageSnooper playerusagesnooper) {
        playerusagesnooper.a("whitelist_enabled", Boolean.valueOf(this.an().n()));
        playerusagesnooper.a("whitelist_count", Integer.valueOf(this.an().h().size()));
        super.a(playerusagesnooper);
    }

    public boolean R() {
        return this.o.a("snooper-enabled", true);
    }

    public void a(String s0, ICommandSender icommandsender) {
        this.k.add(new ServerCommand(s0, icommandsender));
    }

    public void am() {
        while (!this.k.isEmpty()) {
            ServerCommand servercommand = (ServerCommand) this.k.remove(0);

            this.E().a(servercommand.b, servercommand.a);
        }
    }

    public boolean T() {
        return true;
    }

    public DedicatedPlayerList an() {
        return (DedicatedPlayerList) super.ad();
    }

    public NetworkListenThread ae() {
        return this.r;
    }

    public int a(String s0, int i0) {
        return this.o.a(s0, i0);
    }

    public String a(String s0, String s1) {
        return this.o.a(s0, s1);
    }

    public boolean a(String s0, boolean flag0) {
        return this.o.a(s0, flag0);
    }

    public void a(String s0, Object object) {
        this.o.a(s0, object);
    }

    public void a() {
        this.o.b();
    }

    public String b_() {
        File file1 = this.o.c();

        return file1 != null ? file1.getAbsolutePath() : "No settings file";
    }

    public void ao() {
        ServerGUI.a(this);
        this.s = true;
    }

    public boolean ag() {
        return this.s;
    }

    public String a(EnumGameType enumgametype, boolean flag0) {
        return "";
    }

    public boolean Z() {
        return this.o.a("enable-command-block", false);
    }

    public int ak() {
        return this.o.a("spawn-protection", super.ak());
    }

    public boolean a(World world, int i0, int i1, int i2, EntityPlayer entityplayer) {
        if (world.t.h != 0) {
            return false;
        }
        else if (this.an().i().isEmpty()) {
            return false;
        }
        else if (this.an().e(entityplayer.bS)) {
            return false;
        }
        else if (this.ak() <= 0) {
            return false;
        }
        else {
            ChunkCoordinates chunkcoordinates = world.I();
            int i3 = MathHelper.a(i0 - chunkcoordinates.a);
            int i4 = MathHelper.a(i2 - chunkcoordinates.c);
            int i5 = Math.max(i3, i4);

            return i5 <= this.ak();
        }
    }

    public ILogAgent al() {
        return this.l;
    }

    public ServerConfigurationManager ad() {
        return this.an();
    }

    public void reload() {
        /* WorldConfiguration defWorld = Configuration.getWorldConfig(Configuration.getServerConfig().getDefaultWorldName());
         * // this.d = new OPropertyManager(new File("server.properties"));
         * this.y = Configuration.getNetConfig().getBindIp();
         * this.n = Configuration.getNetConfig().isOnlineMode();
         * this.o = defWorld.canSpawnAnimals();
         * this.p = defWorld.canSpawnNpcs();
         * this.q = defWorld.isPvpEnabled();
         * this.r = defWorld.isFlightAllowed();
         * this.s = Configuration.getServerConfig().getMotd();
         * this.z = Configuration.getNetConfig().getPort();
         * this.t = defWorld.getMaxBuildHeight();
         * this.t = (this.t + 8) / 16 * 16;
         * this.t = OMathHelper.a(this.t, 64, 256);
         * // TODO Update worlds (??) */
    }
}