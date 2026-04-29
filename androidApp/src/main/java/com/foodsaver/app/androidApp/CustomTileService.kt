package com.foodsaver.app.androidApp

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import com.foodsaver.app.R

class CustomTileService: TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        performAction()
        updateTile()
    }

    private fun updateTile() {
        this.qsTile?.apply {
            this.icon = Icon.createWithResource(this@CustomTileService, R.drawable.qstile)
            state = Tile.STATE_INACTIVE

            updateTile()
        }
    }

    private fun performAction() {
        Toast.makeText(this, "I'm hungry", Toast.LENGTH_SHORT)
            .show()
    }
}