package com.github.Ringoame196

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace

class Fence {
    fun check(block: Block): Boolean {
        val blockFaceList = listOf(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST)
        for (face in blockFaceList) {
            val adjacentBlock = block.getRelative(face)
            if (adjacentBlock.type == Material.OAK_FENCE) {
                return false
            }
        }
        return true
    }
}
