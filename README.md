# State App

## Dynalist Database format

Put your Routines in `__Task Trees__` node.

### Node types

| Node type | Note format                           | Description                                                                                                          |
|-----------|---------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| Task      | <estimate: Int>                       | Simple task                                                                                                          |
| Routine   | estimate=<estimate>,trigger=<trigger> | Routine task (the task that can be enabled, disabled, or marked not done depending on the trigger type and context). |

### Trigger types

| Trigger type | Description                                                       |
|--------------|-------------------------------------------------------------------|
| Day          | The routine is reset every day (all tasks are marked as not done) |
