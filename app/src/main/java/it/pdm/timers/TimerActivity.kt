package it.pdm.timers

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.pdm.timers.fragments.AddTimerFragment
import it.pdm.timers.fragments.ProfileFragment
import it.pdm.timers.fragments.TimerFragment
import it.pdm.timers.fragments.TimerSalvatiFragment
import kotlinx.android.synthetic.main.activity_timer.*

/**
 * Classe che gestisce la navigation_view e il bottom_navigation_view
 */
class TimerActivity : AppCompatActivity(), Communicator {
    private val timerFragment = TimerFragment()
    private val timerSalvatiFragment = TimerSalvatiFragment()
    private val profileFragment = ProfileFragment()

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, timerFragment).commit()

        val btn_navigation = findViewById<TextView>(R.id.bottom_navigation) as BottomNavigationView
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        auth = Firebase.auth

        //mostra in alto a destra le impostazioni
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener{
            it.isCheckable = true
            when(it.itemId){
                R.id.profile -> openFragment(profileFragment)
                R.id.logout -> logoutMenu(LoginActivity())
            }
            true
        }

        btn_navigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.timer -> openFragment(timerFragment)
                R.id.timer_salvati -> openFragment(timerSalvatiFragment)
            }
            true
        }
    }

    /**
     * Metodo che permette di passare il dato, specificato nell'argomento, alla classe AddTimerFragment
     */
    override fun passData(addItem: Int) {
        val bundle = Bundle()
        bundle.putInt("message", addItem)

        val transaction = this.supportFragmentManager.beginTransaction()
        val addTimerFragment = AddTimerFragment()
        addTimerFragment.arguments = bundle

        transaction.replace(R.id.fragment_container, addTimerFragment)
        transaction.commit()
    }

    /**
     * Metodo che permette di aprire il fragment passato come argomento
     */
    private fun openFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragment_container, fragment)
        fragmentTransition.commit()
        drawerLayout.closeDrawers()
    }

    /**
     * Metodo che permette di aprire la navigation_view
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Metodo che gestisce il logout dall'applicazione
     */
    private fun logoutMenu(activity: LoginActivity){
        val addDialog = AlertDialog.Builder(this)
        addDialog.setTitle("Logout")
        addDialog.setMessage("Sicuro di uscire?")
        addDialog.setPositiveButton("SI"){
                dialog,_->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Log.d(ContentValues.TAG, "Logout")
            dialog.dismiss()
        }
        addDialog.setNegativeButton("NO"){
                dialog,_->
            dialog.dismiss()
            Log.d(ContentValues.TAG, "Cancel")
        }
        addDialog.create()
        addDialog.show()
    }
}