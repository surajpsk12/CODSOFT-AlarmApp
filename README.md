
#⏰ Alarm Clock App - CodSoft Internship Project


A beautiful, modern Alarm Clock App developed as part of my CodSoft Android Development Internship.
Built using Java and follows MVVM architecture, this app supports repeat alarms, labels, custom tones, dark/light theme switching, and more.



📱 Features

- ⏰ **Set and Manage Alarms**
- 🔁 **Repeat Alarms** on specific weekdays (Mon-Sun)
- 🏷️ **Label Support** for each alarm
- 🎵 **Pick Custom Alarm Tones**
- 🌗 **Dynamic Light/Dark Theme Toggle**
- 📲 **Beautiful Full-Screen Alarm UI**
- ✅ **Edit & Delete Existing Alarms**
- 🎨 **Material Design 3 + Yellow-White Theme**
- 🧾 **Snackbar for User Feedback**

🎥 Demo

> 🔗 [Watch the Demo Video](https://www.linkedin.com/posts/surajvansh12_androiddevelopment-java-codsoft-activity-7344327874901917696-KzW7?utm_source=social_share_send&utm_medium=member_desktop_web&rcm=ACoAACcPeWEBxYjxM-NzM1rjVvCEA6sXbwvLGX0)  
> 📦 [Download APK](https://drive.google.com/file/d/1_Utzq7fLVBwi-f7-H8r3RANm4iW_MGgX/view?usp=sharing)



🧱 Tech Stack

| Layer        | Tools / Frameworks                                |
|--------------|---------------------------------------------------|
| Language     | Java                                               |
| UI Design    | Material Design 3, ConstraintLayout                |
| Architecture | MVVM (Model-View-ViewModel)                        |
| Persistence  | Room Database                                     |
| State Mgmt   | ViewModel + LiveData                              |
| UX Enhancers | Snackbar, ChipGroup, AlarmManager, WakeLock APIs  |



🧑‍💻 What I Learned

- Handling full-screen Alarm UIs even when phone is locked
- Using `AlarmManager` with `BroadcastReceiver`
- Displaying Material Chips for day selection
- Switching Light/Dark themes dynamically
- Designing modern UI using Material 3 Components
- Creating persistent data models with Room
- Managing alarm scheduling and snoozing logic



📂 Project Structure

com.surajvanshsv.alarmapp
├── data               # Alarm Entity + DAO + Room Database
├── receiver           # AlarmReceiver for triggering alarms
├── repository         # AlarmRepository for data abstraction
├── ui
│   ├── view           # MainActivity, SetAlarmActivity, AlarmRingActivity
│   ├── viewmodel      # AlarmViewModel
│   └── adapter        # RecyclerView Adapter
└── util               # Converters, Utils


🚀 Getting Started

1. Clone the repo

   
   git clone https://github.com/surajpsk12/CODSOFT-AlarmApp.git

2. Open in Android Studio

3. Run the app on a device or emulator


## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

 🔗 Links

* 📱 [Download APK](https://drive.google.com/file/d/1_Utzq7fLVBwi-f7-H8r3RANm4iW_MGgX/view?usp=sharing)
* 🔗 [GitHub Repo](https://github.com/surajpsk12/CODSOFT-AlarmApp)
* 🌐 [CodSoft Internship Page](https://www.codsoft.in)


🙌 Acknowledgements

* [CodSoft](https://www.codsoft.in) for the internship opportunity
* [Material Design](https://m3.material.io/)
* Android Developer Docs

---

👋 Connect with Me

* 🔗 [LinkedIn](https://www.linkedin.com/in/surajpsk12)
* 💌 [surajpsk12@gmail.com](mailto:sk658139@gmail.com)


📄 License
This project is licensed under the MIT License.

