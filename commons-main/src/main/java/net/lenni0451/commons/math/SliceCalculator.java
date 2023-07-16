package net.lenni0451.commons.math;

import java.util.List;

/**
 * Take a list and calculate slices of it.
 *
 * @param <T> The type of the list
 */
@SuppressWarnings("unused")
public class SliceCalculator<T> {

    private final List<T> list;
    private final int sliceSize;

    public SliceCalculator(final List<T> list, final int sliceSize) {
        this.list = list;
        this.sliceSize = sliceSize;
    }

    /**
     * @return The list
     */
    public List<T> getList() {
        return this.list;
    }

    /**
     * @return The amount of slices
     */
    public int getSliceCount() {
        return MathUtils.ceilInt((double) this.list.size() / this.sliceSize);
    }

    /**
     * Get a slice of the list.
     *
     * @param sliceIndex The index of the slice
     * @return The slice
     * @throws IndexOutOfBoundsException If the slice index is out of bounds
     */
    public List<T> getSlice(final int sliceIndex) {
        if (sliceIndex < 0 || sliceIndex >= this.getSliceCount()) throw new IndexOutOfBoundsException();

        int firstIndex = this.sliceSize * sliceIndex;
        return this.list.subList(firstIndex, Math.min(this.list.size(), firstIndex + this.sliceSize));
    }

}
