package ru.internetcloud.workorderapplication.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.internetcloud.workorderapplication.data.entity.*

@Database(
    entities = [WorkOrderDbModel::class, RepairTypeDbModel::class, CarJobDbModel::class, DepartmentDbModel::class,
        EmployeeDbModel::class, PartnerDbModel::class, CarDbModel::class, WorkingHourDbModel::class,
        PerformerDetailDbModel::class, JobDetailDbModel::class, DefaultWorkOrderSettingsDbModel::class,
        CarModelDbModel::class, DefaultRepairTypeJobDetailDbModel::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "work_order.db"
        private var Instance: AppDatabase? = null

        private val Lock = Any()

        fun getInstance(application: Application): AppDatabase {
            Instance?.let {
                return it
            }
            synchronized(Lock) {
                Instance?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                Instance = db
                return db
            }
        }
    }

    abstract fun appDao(): AppDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'cars' ADD COLUMN 'carModelId' TEXT NOT NULL DEFAULT ' ' ")

//        database.execSQL("BEGIN TRANSACTION;\n" +
//                // "CREATE TEMPORARY TABLE t1_backup(a,b);\n" +
//                "CREATE TEMPORARY TABLE 't1_backup' " +
//                "('id' TEXT NOT NULL, " +
//                "'code1C' TEXT NOT NULL, " +
//                "'name' TEXT NOT NULL, " +
//                "'vin' TEXT NOT NULL, " +
//                "'manufacturer' TEXT NOT NULL, " +
//                "'model' TEXT NOT NULL, " +
//                "'type' TEXT NOT NULL, " +
//                "'releaseYear' INTEGER NOT NULL, " +
//                "'mileage' INTEGER NOT NULL, " +
//                "'ownerId' TEXT NOT NULL " +
//                ");" +
//                "INSERT INTO t1_backup SELECT * FROM cars;\n" +
//                "DROP TABLE cars;\n" +
//            "CREATE TABLE 'cars' " +
//                "('id' TEXT NOT NULL, " +
//                "'code1C' TEXT NOT NULL, " +
//                "'name' TEXT NOT NULL, " +
//                "'vin' TEXT NOT NULL, " +
//                "'manufacturer' TEXT NOT NULL, " +
//                "'carModelId' TEXT NOT NULL, " +
//                "'type' TEXT NOT NULL, " +
//                "'releaseYear' INTEGER NOT NULL, " +
//                "'mileage' INTEGER NOT NULL, " +
//                "'ownerId' TEXT NOT NULL, " +
//                "PRIMARY KEY('id')); " +
//                "INSERT INTO cars " +
//                "(id, code1C, name, vin, manufacturer, type, releaseYear, mileage, ownerId) " +
//                "SELECT " +
//                "id, code1C, name, vin, manufacturer," +
//                "type, releaseYear, mileage, ownerId FROM t1_backup; \n" +
//                "DROP TABLE t1_backup; \n" +
//                "COMMIT; " +
//                "")

        database.execSQL("CREATE TABLE car_models (" +
                "id TEXT NOT NULL DEFAULT ' '," +
                "code1C TEXT NOT NULL DEFAULT ' '," +
                "name TEXT NOT NULL DEFAULT ' '," +
                "PRIMARY KEY(id)" +
                ")")

        database.execSQL("CREATE TABLE default_repair_type_job_details (" +
                "id TEXT NOT NULL DEFAULT ' '," +
                "lineNumber INTEGER NOT NULL DEFAULT 0," +
                "carModelId TEXT NOT NULL DEFAULT ' '," +
                "carJobId TEXT NOT NULL DEFAULT ' '," +
                "quantity TEXT NOT NULL DEFAULT ' '," +
                "repairTypeId TEXT NOT NULL DEFAULT ' '," +
                "PRIMARY KEY(id)" +
                ")")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'default_work_order_settings' ADD COLUMN 'workingHourId' TEXT NOT NULL DEFAULT ' ' ") // default_work_order_settings  workingHourId  defaultTimeNorm
        database.execSQL("ALTER TABLE 'default_work_order_settings' ADD COLUMN 'defaultTimeNorm' TEXT NOT NULL DEFAULT ' ' ") // default_work_order_settings  workingHourId  defaultTimeNorm
    }
}

