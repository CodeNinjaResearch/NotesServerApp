package name.antonsmirnov.notes.controller.grpc

import com.google.protobuf.StringValue
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.runBlocking
import name.antonsmirnov.notes.usecase.ListNotes

// DTOs and ListNotesControllerImplBase are generated by gradle plugin from IMLs
// (see src/main/proto files)
class ListNotesController(
        private val useCase: ListNotes
) : ListNotesControllerGrpc.ListNotesControllerImplBase() {

    override fun list(request: ListNotesRequest?, responseObserver: StreamObserver<ListNotesResponse>?) = runBlocking {
        // execute interactor
        val response = useCase.execute()

        // map canonical dto back to gRPC dto and return
        val responseGrpc = ListNotesResponse
                .newBuilder()
                .addAllNote(response.notes.map {
                    val noteBuilder = Note
                            .newBuilder()
                            .setId(it.id)
                            .setTitle(it.title)
                    if (it.body != null) {
                        noteBuilder.setBody(
                            StringValue
                                .newBuilder()
                                .setValue(it.body))
                    }
                    noteBuilder.build()
                })
                .build()

        responseObserver!!.onNext(responseGrpc)
        responseObserver.onCompleted()
    }

}