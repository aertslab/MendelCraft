package org.aertslab.mendelcraft.blocks;

import net.minecraft.util.StringRepresentable;

public enum LabPart implements StringRepresentable {
   LEFT("left"),
   RIGHT("right"),
   NS_LOWER("ns_lower"),
   NS_UPPER("ns_upper");

   private final String name;

   private LabPart(String p_61339_) {
      this.name = p_61339_;
   }

   public String toString() {
      return this.name;
   }

   public String getSerializedName() {
      return this.name;
   }
}