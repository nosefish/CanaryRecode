package net.canarymod.api.world.blocks;


import net.canarymod.Canary;
import net.canarymod.api.world.CanaryWorld;
import net.canarymod.api.world.World;


public class CanaryBlock implements Block {
    protected short type, data;
    protected byte status;
    protected int x, y, z;
    protected World dimension;
    protected BlockFace faceClicked;

    public CanaryBlock(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = 0;
        this.data = 0;
        status = 0;
        dimension = Canary.getServer().getDefaultWorld();
    }

    public CanaryBlock(short type, short data) {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.type = type;
        this.data = data;
        status = 0;
        dimension = Canary.getServer().getDefaultWorld();
    }

    public CanaryBlock(short type, short data, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.data = data;
        status = 0;
        dimension = Canary.getServer().getDefaultWorld();
    }

    public CanaryBlock(short type, short data, int x, int y, int z, CanaryWorld canaryWorld) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.data = data;
        status = 0;
        this.dimension = canaryWorld;
    }

    @Override
    public short getType() {
        return type;
    }

    @Override
    public void setType(short type) {
        this.type = type;

    }

    @Override
    public void setType(int type) {
        this.type = (short) type;

    }

    @Override
    public short getData() {
        return data;
    }

    @Override
    public World getDimension() {
        return this.dimension;
    }

    @Override
    public void setDimension(World dimension) {
        this.dimension = dimension;
    }

    @Override
    public BlockFace getFaceClicked() {
        return faceClicked;
    }

    @Override
    public void setFaceClicked(BlockFace face) {
        faceClicked = face;
    }

    @Override
    public void update() {
        dimension.markBlockNeedsUpdate(x, y, z);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public byte getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("Block[type=%d, x=%d, y=%d, z=%d, world=%s, dim=%d]", this.type, this.x, this.y, this.z, this.dimension.getName(), this.dimension.getType().getId());
    }

    /**
     * Tests the given object to see if it equals this object
     * 
     * @param obj
     *            the object to test
     * @return true if the two objects match
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CanaryBlock other = (CanaryBlock) obj;

        if (this.x != other.getX()) {
            return false;
        }
        if (this.y != other.getY()) {
            return false;
        }
        if (this.z != other.getZ()) {
            return false;
        }
        if (!this.getDimension().equals(other.getDimension())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a semi-unique hashcode for this block
     * 
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int hash = 7;

        hash = 97 * hash + this.data;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        hash = 97 * hash + this.z;
        hash = 97 * hash + this.type;
        return hash;
    }
}
