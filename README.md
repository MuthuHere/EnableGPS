# EnableGPS
Enable device GPS programatically.

<p align="center">
  <img src="https://github.com/MuthuHere/EnableGPS/blob/master/ss.png" width="350" height="550" title="hover text">
</p>


## Installation

### If you are using Gradle

Step 1. Add the JitPack repository to your build file
   Add it in your root build.gradle at the end of repositories:

             allprojects {
	            	repositories {
		             	...
		            	maven { url 'https://jitpack.io' }
		             }
	            }

Step 2. Add the dependency

      dependencies {
	            implementation 'com.github.MuthuHere:EnableGPS:1.0.0'
	      }   
        
Step 3. In your MainActivity.kt  implement my interface 
         
      class MainActivity : AppCompatActivity(), EnableMyGps.GpsStatusCallBack {
      	 lateinit var gpsStatus: EnableMyGps
	 override fun onCreate(savedInstanceState: Bundle?) {
	 		...
	    //initialize		
	    gpsStatus = EnableMyGps(this)
	    // check whether gps is on, if not, request will be happen beyond your class
       	    gpsStatus.checkMyGpsStatus()
        }      
         
	override fun onGpsSettingStatus(enabled: Boolean) {
             when (enabled) {
                true -> {
                  //enabled success do your positive stuff here
                 }
                false -> {
                  //enabled failed if needed request again
                }
             }
          }

        override fun onGpsAlertCanceledByUser() {
             // cancelled request if needed try request again
          }
     }

### If you are using Maven

Step 1. Add the JitPack repository to your build file

    <repositories>
		  <repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		  </repository>
	  </repositories>
  
Step 2. Add the dependency
   
      <dependency>
	       <groupId>com.github.MuthuHere</groupId>
	        <artifactId>EnableGPS</artifactId>
	        <version>1.0.0</version>
   	  </dependency>


Thanks for using:) Happie coding:)
