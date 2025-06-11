# State App

## Dynalist Database format

Put your Routines in `__Task Trees__` node.

### Node types

| Node type | Note format                           | Description                                                                                                                                                        |
|-----------|---------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Task      | <estimate: Int>                       | Simple task                                                                                                                                                        |
| Routine   | estimate=<estimate>,trigger=<trigger> | Routine task (the task that can be enabled, disabled, or marked not done depending on the trigger type and context).                                               |
| Flipper   | flipper=true                          | Flipper task (the task that can be enabled or disabled based on some context). The children can use different Schedule logic with `schedule=<schedule type>` param |

### Routine Trigger types

| Trigger type | Description                                                       |
|--------------|-------------------------------------------------------------------|
| Day          | The routine is reset every day (all tasks are marked as not done) |

### Flipper Schedule types

The type name is case-insensitive.

| Schedule type | Description                                                                                                                                              |
|---------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| Squeeze       | The task is disabled if there is no time left for it until the rest of the day. The priority for enabling is for tasks that wasn't done for longer time. |
