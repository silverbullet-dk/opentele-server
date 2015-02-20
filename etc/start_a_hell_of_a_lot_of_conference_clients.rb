require "net/http"
require "uri"

Patient = Struct.new(:user_name, :password)

patients = [
	Patient.new('nancyann', 'abcd1234'),
	Patient.new('linda', 'abcd1234'),
	#Patient.new('erna', 'abcd1234'),
	Patient.new('else', 'abcd1234')
]

threads = []
1000.times do |thread_number|
	threads << Thread.start do
		begin
			patient = patients[thread_number % patients.length]

			uri = URI.parse("http://localhost:8080/opentele-server/rest/conference/patientHasPendingConference")

			http = Net::HTTP.new(uri.host, uri.port)
			request = Net::HTTP::Get.new(uri.request_uri)
			http.read_timeout = 500
			request.basic_auth(patient.user_name, patient.password)

			puts "Thread #{thread_number} (#{patient.user_name}) requesting"
			response = http.request(request)

			puts "Response for thread #{thread_number} (#{patient.user_name}): #{response.code}, #{response.body}"
		rescue SocketError => e
			puts "Error... #{e}"
		end
	end
end

threads.each {|thread| thread.join }
puts 'All done!'