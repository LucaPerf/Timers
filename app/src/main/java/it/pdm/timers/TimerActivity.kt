package it.pdm.timers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import it.pdm.timers.fragments.HomeFragment
import it.pdm.timers.fragments.ProfileFragment
import it.pdm.timers.fragments.TimerFragment
import it.pdm.timers.fragments.TimerSalvatiFragment
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity(), Communicator{
    private val timerFragment = TimerFragment()
    private val timerSalvatiFragment = TimerSalvatiFragment()
    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        replaceFragment(timerFragment)

        val btn_navigation = findViewById<TextView>(R.id.bottom_navigation) as BottomNavigationView
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        val intent = Intent(this, LoginActivity::class.java)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //mostrare in alto a destra le impostazioni
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener{
            it.isCheckable = true
            when(it.itemId){
                R.id.profile -> replaceFragment(profileFragment)
                R.id.settings -> Toast.makeText(applicationContext, "Impostazioni", Toast.LENGTH_SHORT).show()
                R.id.logout -> startActivity(intent)
            }
            true
        }

        btn_navigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.timer -> replaceFragment(timerFragment)
                R.id.timer_salvati -> replaceFragment(timerSalvatiFragment)
                R.id.home -> replaceFragment(homeFragment)
            }
            true
        }
    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragment_container, fragment)
        fragmentTransition.commit()
        drawerLayout.closeDrawers()
    }

    override fun passData(addItem: String) {
        val bundle = Bundle()
        bundle.putString("message", addItem)

        val transaction = this.supportFragmentManager.beginTransaction()
        timerSalvatiFragment.arguments = bundle

        //passiamo i dati al timerfragmentsalvti e apriamo il fragment
        transaction.replace(R.id.fragment_container, timerSalvatiFragment).commit()
    }

    //usata aprire la navigation drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)

    }
}
