package com.spqrta.state.common.environments.tasks_database

object TasksDatabaseEnvironment {
    
    fun getTask(id: String): DatabaseTask? {
        return DatabaseTask("1")
    }

    fun updateTask(task: DatabaseTask) {
        // update task
    }

    private fun loadData(): List<DatabaseTask> {
        return listOf(DatabaseTask("1"), DatabaseTask("2"))
    }

    private fun saveData(data: List<DatabaseTask>) {
        // save data
    }
}