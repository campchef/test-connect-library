/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.shanethompson.cabelastestapplication.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.campchef.iotconnect.CampChefConnectActivity
import edu.shanethompson.cabelastestapplication.MyApplication
import edu.shanethompson.cabelastestapplication.login.LoginActivity
import edu.shanethompson.cabelastestapplication.registration.RegistrationActivity
import com.example.android.dagger.settings.SettingsActivity
import edu.shanethompson.cabelastestapplication.AppDatabase
import edu.shanethompson.cabelastestapplication.Pet
import edu.shanethompson.cabelastestapplication.R
import edu.shanethompson.cabelastestapplication.User
import javax.inject.Inject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var mainViewModel: MainViewModel

    /**
     * If the User is not registered, RegistrationActivity will be launched,
     * If the User is not logged in, LoginActivity will be launched,
     * else carry on with MainActivity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Grabs instance of UserManager from the application graph
        val userManager = (application as MyApplication).appComponent.userManager()
        if (!userManager.isUserLoggedIn()) {
            if (!userManager.isUserRegistered()) {
                startActivity(Intent(this, RegistrationActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        } else {
            setContentView(R.layout.activity_main)

            // If the MainActivity needs to be displayed, we get the UserComponent from the
            // application graph and gets this Activity injected
            userManager.userComponent!!.inject(this)
            setupViews()
        }

        findViewById<View>(R.id.openCabelasConnect).setOnClickListener {
            startActivity(Intent(this, CampChefConnectActivity::class.java))
        }

        val db = Room.databaseBuilder(this.applicationContext, AppDatabase::class.java, "test-db").build()
        thread {
            val allUsers = db.userDao().getAll()
            if (allUsers.isEmpty()) {
                addData(db)
            }
        }
    }

    private fun addData(db: AppDatabase) {
        db.userDao().insertAll(User(1, "first", "last"))
        db.petDao().insertAll(Pet(1, 1, "fido", "dog"), Pet(2, 1, "fluffy", "cat"))
    }

    /**
     * Updating unread notifications onResume because they can get updated on SettingsActivity
     */
    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.notifications).text = mainViewModel.notificationsText
    }

    private fun setupViews() {
        findViewById<TextView>(R.id.hello).text = mainViewModel.welcomeText
        findViewById<Button>(R.id.settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
