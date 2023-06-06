package md.hackaton.aasocialrecovery.screen.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import md.hackaton.aasocialrecovery.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

}